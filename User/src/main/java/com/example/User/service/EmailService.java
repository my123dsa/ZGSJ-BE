package com.example.User.service;


import com.example.User.util.QRCodeUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final QRCodeUtil qrCodeUtil;

    public byte[] sendQRToEmail(String email, Integer storeId) {  // 리턴 타입을 void로 변경
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            byte[] qrImageData = qrCodeUtil.generateQRCodeImage(storeId);

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[집계사장]직원관리 QR");

            // QR 코드를 첨부 파일로도 추가
            helper.addAttachment("QRCode.png", new ByteArrayDataSource(qrImageData, "image/png"));

            InputStreamSource imageSource = new ByteArrayResource(qrImageData);
            helper.addInline("qrImage", imageSource, "image/png");  // ContentType 명시적 지정

            helper.setText(createHTML(), true);

            javaMailSender.send(mimeMessage);
            log.info("QR 코드 이메일 전송 완료: {}", email);
            return qrImageData;
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }

    private String createHTML() {
        return """
                <div style='max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif;'>
                    <div style='background-color: #0068FF; padding: 20px; text-align: center;'>
                        <h1 style='color: white; margin: 0;'>집계사장</h1>
                    </div>
                    <div style='background-color: #ffffff; padding: 40px 20px; text-align: center; border: 1px solid #e9e9e9;'>
                        <h2 style='color: #333333; margin-bottom: 30px;'>직원 QR 이미지</h2>
                        <p style='color: #666666; font-size: 16px; line-height: 24px;'>
                            안녕하세요.<br>직원 관리를 위한 QR코드를 보내드립니다.
                        </p>
                        <div style='background-color: #f8f9fa; padding: 15px; margin: 30px auto; max-width: 300px; border-radius: 4px;'>
                            <img src='cid:qrImage' alt='QR Code' style='max-width: 200px; max-height: 200px;'/>
                        </div>
                    </div>
                    <div style='text-align: center; padding: 20px; color: #999999; font-size: 12px;'>
                        <p>본 메일은 발신전용 메일입니다.</p>
                        <p>&copy; 2024 CoreBank. All rights reserved.</p>
                    </div>
                </div>
                """;
    }
}