package com.example.User.service;

import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.repository.PresidentRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
public class PasswordEmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PresidentRepository presidentRepository;

    private String authPassword;

    // 임의의 비밀번호 생성
    public void makeRandomPassword() {
        final String CHAR_SET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        final int PASSWORD_LENGTH = 10;
        SecureRandom rand = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for(int i = 0; i < PASSWORD_LENGTH; i++) {
            int randIdx = rand.nextInt(CHAR_SET.length());
            password.append(CHAR_SET.charAt(randIdx));
        }
        authPassword = password.toString();
    }

    // 이메일과 이름 확인
    public boolean validateEmailAndName(String email, String name) {
        Optional<President> president = presidentRepository.findByEmail(email);
        if (president.isPresent() && president.get().getName().equals(name)) {
            return true;
        } else {
            return false;
        }
    }

    // mail 양식 설정
    public String joinEmail(String email) {
        makeRandomPassword();
        String setForm = "${EMAIL_USERNAME}@gmail.com"; // email-config에 설정한 내 이메일 주소
        String toMail = email;
        String title = "[집계사장] 임시 비밀번호를 보내드립니다."; // 이메일 제목
        String content =
                "집계사장을 사용해주셔서 감사합니다. 🦀🍔🍟" +
                        "<br><br> " +
                        "임시 비밀번호는 " + authPassword + "입니다." +
                        "<br> " +
                        "보안을 위해 로그인 후에는 꼭 비밀번호를 변경해주세요!"; // 이메일 내용
        mailSend(setForm, toMail, title, content);
        return authPassword;
    }

    private void mailSend(String setForm, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage(); // MinmeMessage 객체 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setForm); // 이메일 발신자 주소 설정
            helper.setTo(toMail); // 이메일 수신자 주소 설정
            helper.setSubject(title); // 이메일 주소 설정
            helper.setText(content, true); // 이메일의 내용
            mailSender.send(message);
        } catch (MessagingException e) {
            if(e.getMessage().contains("Invalid Addresses")) {
                throw new CustomException(ErrorCode.INVALID_EMAIL);
            } else {
                log.error("이메일 전송 실패: {}", e.getMessage());
                throw new CustomException(ErrorCode.SERVER_ERROR);
            }
        }
    }

    public void updatePassword(String str, String email) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(str); // 패스워드 암호화
            Optional<President> president = presidentRepository.findByEmail(email);
            if (president.isPresent()) {
                President existingPresident = president.get();
                existingPresident.setPassword(encodedPassword);
                presidentRepository.save(existingPresident);
            } else {
                throw new CustomException(ErrorCode.PRESIDENT_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
