package com.example.User.controller;

import com.example.User.dto.passwordemail.EmailRequest;
import com.example.User.service.PasswordEmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/president")
public class PasswordEmailController {

    private final PasswordEmailService passwordEmailService;

    @PostMapping("/resetPassword")
    public ResponseEntity<String> ResetPassword(@RequestBody @Valid EmailRequest emailRequest) {
        System.out.println("이메일 인증 이메일: " + emailRequest.getEmail());
        // 이메일과 이름이 일치하는지 확인
        if (passwordEmailService.validateEmailAndName(emailRequest.getEmail(), emailRequest.getName())) {

            // 임시 비밀번호 생성하고 이메일 전송
            String temporaryPassword = passwordEmailService.joinEmail(emailRequest.getEmail());

            // 임시 비밀번호로 DB 업데이트
            passwordEmailService.updatePassword(temporaryPassword, emailRequest.getEmail());

            return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("이름과 이메일이 일치하지 않습니다.");
        }
    }

}
