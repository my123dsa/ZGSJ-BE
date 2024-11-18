package com.example.User.controller;

import com.example.User.dto.passwordemail.EmailRequest;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/president")
@Slf4j
public class PasswordEmailController {

    private final EmailService emailService;

    @PostMapping("/resetPassword")
    public ResponseEntity<String> ResetPassword(@RequestBody @Valid EmailRequest emailRequest) {
        log.info("이메일 인증 이메일: " + emailRequest.getEmail());
        // 이메일과 이름이 일치하는지 확인
        if (!emailService.validateEmailAndName(emailRequest.getEmail(), emailRequest.getName())) {
            throw new CustomException(ErrorCode.MISMATCH_EMAIL);
        }
        // 임시 비밀번호 생성하고 이메일 전송
        String temporaryPassword = emailService.joinEmail(emailRequest.getEmail());

        // 임시 비밀번호로 DB 업데이트
        emailService.updatePassword(temporaryPassword, emailRequest.getEmail());

        return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
    }

}
