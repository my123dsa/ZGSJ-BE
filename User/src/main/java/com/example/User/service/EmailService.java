package com.example.User.service;


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

    public String sendURLToEmail(String email, Integer storeId,String encryptedEmail) {  // 리턴 타입을 void로 변경
        try {

            String ip="localhost";
            String url = String.format("http://%s:8888/%d/commute/%s",ip, storeId, encryptedEmail);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[집계사장]직원 URL");

            helper.setText(createHTML(url), true);

            javaMailSender.send(mimeMessage);
            log.info("이메일 전송 완료: {}", email);
            return url;
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new RuntimeException("메일 발송에 실패했습니다.", e);
        }
    }

    private String createHTML(String url) {
        return """
            <div style='max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif;'>
                <div style='background-color: #0068FF; padding: 20px; text-align: center;'>
                    <h1 style='color: white; margin: 0;'>집계사장</h1>
                </div>
                <div style='background-color: #ffffff; padding: 40px 20px; text-align: center; border: 1px solid #e9e9e9;'>
                    <h2 style='color: #333333; margin-bottom: 30px;'>직원 등록 링크</h2>
                    <p style='color: #666666; font-size: 16px; line-height: 24px;'>
                        안녕하세요.<br>출퇴근을 위한 링크를 보내드립니다.
                    </p>
                    <div style='background-color: #f8f9fa; padding: 15px; margin: 30px auto; max-width: 500px; border-radius: 4px;'>
                        <p style='word-break: break-all; color: #666666;'>%s</p>
                    </div>
                    <a href='%s' style='display: inline-block; background-color: #0068FF; color: white; padding: 15px 30px; text-decoration: none; border-radius: 5px; margin-top: 20px; font-weight: bold;'>
                        출퇴근 링크
                    </a>
                </div>
                <div style='text-align: center; padding: 20px; color: #999999; font-size: 12px;'>
                    <p>본 메일은 발신전용 메일입니다.</p>
                    <p>&copy; 2024 CoreBank. All rights reserved.</p>
                </div>
            </div>
            """.formatted(url, url);  // url을 두 곳에 삽입
    }
}