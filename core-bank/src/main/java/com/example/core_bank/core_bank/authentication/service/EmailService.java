package com.example.core_bank.core_bank.authentication.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public String sendVerificationEmail(String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String key = createKey();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[CoreBank] 이메일 인증번호");

            String htmlContent = createHTML(key);

            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            return key;
        } catch (MessagingException e) {
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }

    public String createHTML(String key) {
        return String.format(
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif;'>" +
                        "<div style='background-color: #0068FF; padding: 20px; text-align: center;'>" +
                        "<h1 style='color: white; margin: 0;'>CoreBank</h1>" +
                        "</div>" +
                        "<div style='background-color: #ffffff; padding: 40px 20px; text-align: center; border: 1px " +
                        "solid #e9e9e9;'>" +
                        "<h2 style='color: #333333; margin-bottom: 30px;'>이메일 인증번호</h2>" +
                        "<p style='color: #666666; font-size: 16px; line-height: 24px;'>안녕하세요.<br>아래의 인증번호를 입력해주세요" +
                        ".</p>" +
                        "<div style='background-color: #f8f9fa; padding: 15px; margin: 30px auto; max-width: 300px; " +
                        "border-radius: 4px;'>" +
                        "<h3 style='color: #0068FF; letter-spacing: 5px; font-size: 32px; margin: 0;'>%s</h3>" +
                        "</div>" +
                        "<p style='color: #999999; font-size: 14px;'>인증번호는 5분간 유효합니다.</p>" +
                        "</div>" +
                        "<div style='text-align: center; padding: 20px; color: #999999; font-size: 12px;'>" +
                        "<p>본 메일은 발신전용 메일입니다.</p>" +
                        "<p>&copy; 2024 CoreBank. All rights reserved.</p>" +
                        "</div>" +
                        "</div>", key
        );
    }

    public String createKey() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}
