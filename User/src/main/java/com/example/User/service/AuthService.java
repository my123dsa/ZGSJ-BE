package com.example.User.service;

import com.example.User.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final JWTUtil jwtUtil;
    private final RedisTokenService tokenService;

    public String onAuthenticationSuccess(String email) {

        log.info("email" + email);

        //원래 1일 또는 2일 으로 설정해야함
        String accessToken = jwtUtil.generateToken(email, 100);
        String refreshToken = jwtUtil.generateToken(email, 1000);

        tokenService.setValues(email, refreshToken, Duration.ofDays(1));

        return accessToken;
    }
}
