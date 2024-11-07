package com.example.API_Gateway.filter;


import com.example.API_Gateway.error.exception.AccessTokenException;
import com.example.API_Gateway.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;


@Slf4j
@Component
public class TokenCheckFilter implements GlobalFilter, Ordered {

    private final JWTUtil jwtUtil;

    public TokenCheckFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        log.info("TokenCheckFilter path: {}", path);

        // /user/login 경로는 예외 처리
        if (path.contains("/login") ||
                path.contains("/regist") ||
                path.contains("/logout") ||
                path.contains("/refresh") ||
                path.contains("/QRCheck")) {
            log.info("통과");
            return chain.filter(exchange);
        }

        log.info("Token Check Filter..........................");

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.info("헤더에 정보가 없습니다");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        String tokenStr = request.getHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0)
                .replace("Bearer ", "");

        log.info("tokenStr: {}", tokenStr);
        try {
            Map<String, Object> payload = validateAccessToken(tokenStr);
            if (payload.containsKey("refresh")) {
                //상태 코드 지정
                return onError(exchange, HttpStatus.BAD_GATEWAY);
            }

            String email = jwtUtil.decrypt((String) payload.get("payload"));
            log.info("email: {}", email);

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("email", email)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (AccessTokenException e) {
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
    }

    private Map<String, Object> validateAccessToken(String tokenStr) throws AccessTokenException {
        try {
            return jwtUtil.validateToken(tokenStr);
        } catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        } catch (SignatureException signatureException) {
            log.error("SignatureException----------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("ExpiredJwtException----------------------");
            Map<String, Object> claims = expiredJwtException.getClaims();
            claims.put("refresh", "true");
            return claims;
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}