package com.example.API_Gateway;

import com.example.API_Gateway.filter.TokenCheckFilter;
import com.example.API_Gateway.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 프리플라이트 요청 캐시 시간 (초)

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil) {
        List<String> permitUrls = new ArrayList<>();
        permitUrls.add("/president/login");
        permitUrls.add("/president/regist");
        permitUrls.add("commute/QRCheck");
        permitUrls.add("/leave-work");
        permitUrls.add("/go-to-work");
        permitUrls.add("/president/findPassword"); // id 찾기는 인증 없이도 가능하도록 해야하니까

        List<String> needIdUrls = new ArrayList<>();
        needIdUrls.add("/president/logout");
        needIdUrls.add("/president/refresh");
        needIdUrls.add("/president/modify");
        needIdUrls.add("/president/change-password");
        needIdUrls.add("/president/account-check");
        needIdUrls.add("/user/store");

        return new TokenCheckFilter(jwtUtil, permitUrls, needIdUrls);
    }
}
