package com.example.User.service;

import com.example.User.config.UserConfig;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.repository.PresidentRepository;
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
    private final PresidentRepository presidentRepository;
    private final UserConfig userConfig;

    public String onAuthenticationSuccess(Integer id) {
        log.info("id {}", id);
        //원래 1일 또는 2일 으로 설정해야함
        String accessToken = jwtUtil.generateToken(id, 100);
        String refreshToken = jwtUtil.generateToken(id, 1000);

        tokenService.setValues(id, refreshToken, Duration.ofDays(1));

        return accessToken;
    }

    public boolean validatePassword(Integer id, String passwordclaim)
    {
        President president =  presidentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRESIDENT_NOT_FOUND));

       if (!userConfig.passwordEncoder().matches(passwordclaim, president.getPassword()))
       {
           throw new CustomException(ErrorCode.PASSWORD_WRONG);
       }

       return true;
    }
}
