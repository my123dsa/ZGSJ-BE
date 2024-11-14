package com.example.API_Gateway.filter;


import com.example.API_Gateway.error.CustomException;
import com.example.API_Gateway.error.ErrorCode;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Slf4j
public class TokenCheckFilter implements GlobalFilter, Ordered {

    private final JWTUtil jwtUtil;
    private  final List<String> permitUrl;
    private  final List<String> needIdUrl;

    public TokenCheckFilter(JWTUtil jwtUtil, List<String> permitUrl, List<String> needIdUrl) {
        this.jwtUtil = jwtUtil;
        this.permitUrl = permitUrl;
        this.needIdUrl = needIdUrl;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String path = request.getURI().getPath();
        log.info("TokenCheckFilter path: {}", path);

        if(permitUrl.stream().anyMatch(path::contains)) {
            log.info("통과");
            return chain.filter(exchange);
        }

        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            log.info("헤더에 정보가 없습니다");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        String tokenStr = headers.get(HttpHeaders.AUTHORIZATION)
                .get(0).replace("Bearer ", "");

        Map<String, Object> payload = validateAccessToken(tokenStr,path);

        if(!needIdUrl.stream().anyMatch(path::contains)) {
            log.info("통과");
            return chain.filter(exchange);
        }

        // 숫자인지 한번 확인 필요할 수도 //필요한 것만 복호화 시키기
        Integer id = jwtUtil.decrypt((String) payload.get("payload"));
        log.info("id: {}", id);

        ServerHttpRequest mutatedRequest = request.mutate()
                .header("id", id.toString())
                .build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private Map<String, Object> validateAccessToken(String tokenStr,String path){
        try {
            return jwtUtil.validateToken(tokenStr);
        } catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException----------------------");
            throw new CustomException(ErrorCode.MALFORM_TOKEN);
        } catch (SignatureException signatureException) {
            log.error("SignatureException----------------------");
            throw new CustomException(ErrorCode.BADSIGN_TOKEN);
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("ExpiredJwtException----------------------");
            if(path.contains("/president/refresh"))
                return expiredJwtException.getClaims();
            throw  new CustomException(ErrorCode.BAD_GATEWAY_TEST);
//            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }
}