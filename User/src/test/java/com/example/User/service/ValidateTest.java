package com.example.User.service;

import com.example.User.config.MailConfig;
import com.example.User.config.UserConfig;
import com.example.User.error.CustomException;
import com.example.User.model.President;
import com.example.User.repository.PresidentRepository;
import com.example.User.util.JWTUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // SpringBootTest 대신 MockitoExtension 사용
class ValidateTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private RedisTokenService tokenService;

    @Mock
    private PresidentRepository presidentRepository;

    @Mock
    private UserConfig userConfig;


    //실제로 encoder사용하기
    private BCryptPasswordEncoder realPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("비밀번호 인증 테스트 - 실제 인코딩")
    public void successValidPasswordWithRealEncoder() {
        // Given
        Integer userId = 1;
        String rawPassword = "qwer1234!";
        String encodedPassword = realPasswordEncoder.encode(rawPassword);

        President president = President.createPresident(
                "테스트", "테스트", "테스트이메일", encodedPassword,
                LocalDate.of(2024, 1, 22),
                "010-1234-5123", true
        );

        when(presidentRepository.findById(userId)).thenReturn(Optional.of(president));
        when(userConfig.passwordEncoder()).thenReturn(realPasswordEncoder);

        assertThat(authService.validatePassword(userId, rawPassword)).isTrue();
    }

    @Test
    @DisplayName("비밀번호 인증 테스트 -실패")
    public void failValidPasswordWithRealEncoder() {
        // Given
        Integer userId = 1;
        String rawPassword = "qwer1234!";
        String encodedPassword = realPasswordEncoder.encode(rawPassword);

        President president = President.createPresident(
                "테스트", "테스트", "테스트이메일", encodedPassword,
                LocalDate.of(2024, 1, 22),
                "010-1234-5123", true
        );

        when(presidentRepository.findById(userId)).thenReturn(Optional.of(president));
        when(userConfig.passwordEncoder()).thenReturn(realPasswordEncoder);

        String password = "dowrong1234!";

        assertThrows(CustomException.class, () -> {
            authService.validatePassword(userId, password);
        });
    }

}