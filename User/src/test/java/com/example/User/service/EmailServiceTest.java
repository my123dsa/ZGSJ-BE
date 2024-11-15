package com.example.User.service;


import com.example.User.util.QRCodeUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {


    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private QRCodeUtil qrCodeUtil;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Test
    @DisplayName("QR 코드 이메일 전송 성공 테스트")
    void sendQRToEmail_Success() throws Exception {
        // Given
        String email = "test@example.com";
        Integer storeId = 123;
        byte[] mockQrImageData = "mock-qr-data".getBytes();

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(qrCodeUtil.generateQRCodeImage(storeId)).thenReturn(mockQrImageData);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        // When
        byte[] result = emailService.sendQRToEmail(email, storeId);

        // Then
        assertThat(result).isEqualTo(mockQrImageData);
        verify(javaMailSender).createMimeMessage();
        verify(qrCodeUtil).generateQRCodeImage(storeId);
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("이메일 전송 실패시 예외 발생 테스트")
    void sendQRToEmail_ThrowsException() throws Exception {
        String email = "test@example.com";
        Integer storeId = 123;
        byte[] mockQrImageData = "test-qr-data".getBytes();

        // MimeMessageHelper 생성 시 예외 발생
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(qrCodeUtil.generateQRCodeImage(storeId)).thenReturn(mockQrImageData);

        // PowerMockito를 사용하여 MimeMessageHelper 생성을 모킹
        doAnswer(invocation -> {
            throw new MessagingException("메일 발송에 실패했습니다");
        }).when(javaMailSender).send(mimeMessage);

        // When & Then
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () ->
                emailService.sendQRToEmail(email, storeId)
        );

        assertThat(exception)
                .hasMessage("메일 발송에 실패했습니다.")
                .hasCauseInstanceOf(MessagingException.class);


        verify(javaMailSender).createMimeMessage();
        verify(qrCodeUtil).generateQRCodeImage(storeId);
        verify(javaMailSender).send(mimeMessage);
    }
}
