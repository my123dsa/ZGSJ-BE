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

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendPayStatement(String email, String url) {
        String title = "[집계사장]직원 급여명세서";
        String content =
                "집계사장을 사용해주셔서 감사합니다. 🦀🍔🍟" +
                        "<br><br> " +
                        "급여 명세서 url은 " + url + "입니다." +
                        "<br> "; // 이메일 내용
        mailSend(email, title, content);
    }

    public void sendBankFail(String email, String name , LocalDate date,String message){
        String title = "[집계사장]직원 자동 이체 실패";
        String content =
                "집계사장을 사용해주셔서 감사합니다. 🦀🍔🍟" +
                        "<br><br> " +
                        "자동이체는"+message+ "로 인해 "+name+"의" + date +"날짜의 자동이체가 실패했습니다." +
                        "<br> "; // 이메일 내용
        mailSend(email, title, content);
    }

    public void sendPdfFail(String email, String name , LocalDate date,String message){
        String title = "[집계사장]직원 급여 명세서 발급 실패";
        String content =
                "집계사장을 사용해주셔서 감사합니다. 🦀🍔🍟" +
                        "<br><br> " +
                        "급여명세서는 "+message+ "로 인해 "+name+"의" + date +"날짜의 급여명세서 발급에 실패했습니다." +
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
            log.info(" {}에게 급여명세서: {} 전송에 성공했습니다:", toMail, content);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }
}