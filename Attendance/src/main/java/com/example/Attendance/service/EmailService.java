package com.example.Attendance.service;



import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendPayStatement(String email,String url){
        String title ="[집계사장]직원 PinNumber";
        String content =
                "집계사장을 사용해주셔서 감사합니다. 🦀🍔🍟" +
                        "<br><br> " +
                        "급여 명세서 url은 " +url+ "입니다." +
                        "<br> "; // 이메일 내용
        mailSend(email, title, content);

    }

    private void mailSend(String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage(); // MimeMessage 객체 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setTo(toMail); // 이메일 수신자 주소 설정
            helper.setSubject(title); // 이메일 주소 설정
            helper.setText(content, true); // 이메일의 내용
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }
}