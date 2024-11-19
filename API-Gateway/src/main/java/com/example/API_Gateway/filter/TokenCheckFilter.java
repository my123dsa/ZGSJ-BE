package com.example.API_Gateway.filter;


import com.example.API_Gateway.error.CustomException;
import com.example.API_Gateway.error.ErrorCode;
import com.example.API_Gateway.util.CryptoUtil;
import com.example.API_Gateway.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
public class TokenCheckFilter implements GlobalFilter, Ordered {

    private final JWTUtil jwtUtil;
    private final CryptoUtil cryptoUtil;
    private  final List<String> permitUrl;
    private  final List<String> needIdUrl;

    public TokenCheckFilter(JWTUtil jwtUtil, List<String> permitUrl, List<String> needIdUrl,CryptoUtil cryptoUtil) {
        this.jwtUtil = jwtUtil;
        this.permitUrl = permitUrl;
        this.needIdUrl = needIdUrl;
        this.cryptoUtil = cryptoUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String path = request.getURI().getPath();
        log.info("TokenCheckFilter path: {}", path);

        if(permitUrl.stream().anyMatch(path::contains)) {
            log.info("인증 미필요 url통과");
            return chain.filter(exchange);
        }

        String tokenStr = Optional.ofNullable(headers.get(HttpHeaders.AUTHORIZATION))
                .flatMap(authHeaders -> authHeaders.stream().findFirst())
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED))
                .replace("Bearer ", "");

        Map<String, Object> payload = jwtUtil.validateToken(tokenStr,path);

        if(!needIdUrl.stream().anyMatch(path::contains)) {
            log.info("id 필요 리스트 통과");
            return chain.filter(exchange);
        }

        // 숫자인지 한번 확인 필요할 수도 //필요한 것만 복호화 시키기
        Integer id = cryptoUtil.decrypt((String) payload.get("payload"));
        log.info("id: {}", id);

        ServerHttpRequest mutatedRequest = request.mutate()
                .header("id", id.toString())
                .build();
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
//    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
//        exchange.getResponse().setStatusCode(status);
//        return exchange.getResponse().setComplete();
//    }
}