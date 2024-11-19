package com.example.User.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {


    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Test
    @DisplayName("QR 코드 이메일 전송 성공 테스트")
    void sendQRToEmail_Success() throws Exception {
        // Given
        String email = "test@example.com";
        String encryptedEmail = "encryptedEmail";
        Integer storeId = 123;
        String url = "http://localhost:8888/123/commute/encryptedEmail";


        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        // When
        String result = emailService.sendURLToEmail(email, storeId,encryptedEmail);

        // Then
        assertThat(result).isEqualTo(url);
        verify(javaMailSender).createMimeMessage();

        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("이메일 전송 실패시 예외 발생 테스트")
    void sendQRToEmail_ThrowsException() throws Exception {
        String email = "test@example.com";
        Integer storeId = 123;
        String encryptedEmail = "encryptedEmail";


        // MimeMessageHelper 생성 시 예외 발생
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // PowerMockito를 사용하여 MimeMessageHelper 생성을 모킹
        doAnswer(invocation -> {
            throw new MessagingException("메일 발송에 실패했습니다");
        }).when(javaMailSender).send(mimeMessage);

        // When & Then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () ->
                emailService.sendURLToEmail(email, storeId,encryptedEmail)
        );

        assertThat(exception)
                .hasMessage("메일 발송에 실패했습니다.")
                .hasCauseInstanceOf(MessagingException.class);


        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(mimeMessage);
    }
}
