package com.example.User.service;

import com.example.User.dto.login.ReqLoginData;
import com.example.User.dto.president.PresidentInfoResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.repository.PresidentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class PresidentServiceTest {

    @Mock
    private PresidentRepository presidentRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    @InjectMocks
    private PresidentService presidentService;

    @Test
    @DisplayName("로그인 성공 테스트")
    void validateLogin_Success() {
        // Given
        ReqLoginData reqLoginData = new ReqLoginData();
        reqLoginData.setPassword("password123");
        reqLoginData.setEmail("nonexistent@email.com");
        President president = President.createPresident(
                "홍길동",                         // name
                "서울시 강남구",                   // address
                "test@email.com",               // email
                "encodedPassword",              // password
                LocalDate.of(1990, 1, 1),      // birthDate
                "01012345678",                 // phoneNumber
                true                           // termsAccept
        );
        when(presidentRepository.findByEmail(reqLoginData.getEmail()))
                .thenReturn(Optional.of(president));
        when(passwordEncoder.matches(reqLoginData.getPassword(), president.getPassword()))
                .thenReturn(true);

        // When
        Integer result = presidentService.validateLogin(reqLoginData);

        // Then
        assertThat(result).isEqualTo(president.getId());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시도 시 예외 발생")
    void validateLogin_UserNotFound() {
        // Given
        ReqLoginData reqLoginData = new ReqLoginData();
        reqLoginData.setPassword("password123");
        reqLoginData.setEmail("nonexistent@email.com");
        when(presidentRepository.findByEmail(reqLoginData.getEmail()))
                .thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                presidentService.validateLogin(reqLoginData)
        );

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도 시 예외 발생")
    void validateLogin_PasswordNotCorrect() {
        // Given
        ReqLoginData reqLoginData = new ReqLoginData();
        reqLoginData.setPassword("password123");
        reqLoginData.setEmail("nonexistent@email.com");
        President president = President.createPresident(
                "홍길동",                         // name
                "서울시 강남구",                   // address
                "test@email.com",               // email
                "encodedPassword",              // password
                LocalDate.of(1990, 1, 1),      // birthDate
                "01012345678",                 // phoneNumber
                true                           // termsAccept
        );
        when(presidentRepository.findByEmail(reqLoginData.getEmail()))
                .thenReturn(Optional.of(president));
        when(passwordEncoder.matches(reqLoginData.getPassword(), president.getPassword()))
                .thenReturn(false);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                presidentService.validateLogin(reqLoginData)
        );

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_NOT_CORRECT);
    }

    @Test
    @DisplayName("마이페이지 검색 성공")
    void mypageSuccess()
    {
        President president = President.createPresident(
                "테스트",
                "dealon",
                "1234",
                "주소",
                LocalDate.of(2024, 5, 11),
                "010-1231-1109",
                true
        );

        when(presidentRepository.findById(1))
                .thenReturn(Optional.of(president));

        PresidentInfoResponse response = presidentService.getPresidentInfo(1);

        assertThat(response.getName()).isEqualTo("테스트");
        assertThat(response.getEmail()).isEqualTo("1234");
        assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(2024, 5, 11));
        assertThat(response.getPhoneNumber()).isEqualTo("010-1231-1109");
    }

}
