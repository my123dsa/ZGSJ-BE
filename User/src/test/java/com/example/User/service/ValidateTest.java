package com.example.User.service;

import com.example.User.error.CustomException;
import com.example.User.model.President;
import com.example.User.repository.PresidentRepository;
import com.example.User.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)  // SpringBootTest 대신 MockitoExtension 사용
class ValidateTest {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private RedisTokenService redisTokenService;

    @Mock
    private PresidentRepository presidentRepository;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private AuthService authService;

    @BeforeEach
    public void setup() {
        this.authService = new AuthService(jwtUtil, redisTokenService, presidentRepository, passwordEncoder);
    }


    @Test
    @DisplayName("비밀번호 인증 테스트 - 실제 인코딩")
    public void successValidPasswordWithRealEncoder() {
        // Given
        Integer userId = 1;
        String rawPassword = "qwer1234!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        President president = President.createPresident(
                "테스트", "테스트", "테스트이메일", encodedPassword,
                LocalDate.of(2024, 1, 22),
                "010-1234-5123", true
        );

        when(presidentRepository.findById(userId)).thenReturn(Optional.of(president));

        assertThat(authService.validatePassword(userId, rawPassword)).isTrue();
    }

    @Test
    @DisplayName("비밀번호 인증 테스트 -실패")
    public void failValidPasswordWithRealEncoder() {
        // Given
        Integer userId = 1;
        String rawPassword = "qwer1234!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        President president = President.createPresident(
                "테스트", "테스트", "테스트이메일", encodedPassword,
                LocalDate.of(2024, 1, 22),
                "010-1234-5123", true
        );

        when(presidentRepository.findById(userId)).thenReturn(Optional.of(president));

        String password = "dowrong1234!";

        assertThrows(CustomException.class, () -> {
            authService.validatePassword(userId, password);
        });
    }

}