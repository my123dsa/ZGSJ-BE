package com.example.User.controller;


import com.example.User.dto.login.ReqLoginData;
import com.example.User.dto.login.ReqRegist;
import com.example.User.dto.login.ResNewAccessToken;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;

import com.example.User.service.AuthService;
import com.example.User.service.PresidentService;
import com.example.User.service.RedisTokenService;
import com.example.User.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/president")
@Slf4j
@RequiredArgsConstructor
public class PresidentController {
    private final RedisTokenService redisTokenService;
    private final JWTUtil jwtUtil;
    private final AuthService authService;
    private final PresidentService presidentService;

    @PostMapping("/login")
    ResponseEntity<ResNewAccessToken> login(@RequestBody ReqLoginData reqLoginData) {
        log.info("reqLoginData: "+reqLoginData);
        // 사용자 로그인 체크 로직 필요
        if (!presidentService.validateLogin(reqLoginData)){
            throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
        }

        String accessToken = authService.onAuthenticationSuccess(reqLoginData.getEmail());
        return ResponseEntity.ok(ResNewAccessToken.from(accessToken));
    }

    @GetMapping("/refresh")
    ResponseEntity<ResNewAccessToken> refresh(HttpServletRequest request) {
        String token=request.getHeader("Authorization")
                .replace("Bearer ", "");
        log.info("token: "+token);

        String email= jwtUtil.getEmailFromToken(token);
        log.info("email: "+email);

        String accessToken =redisTokenService.checkRefreshToken(email);

        return ResponseEntity.ok(ResNewAccessToken.from(accessToken));
    }

    @PostMapping("/regist")
    ResponseEntity<Void> regist(@RequestBody ReqRegist reqRegist){
        log.info("reqRegist: "+reqRegist);
        presidentService.regist(reqRegist);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/secession")
    public ResponseEntity<Void> secession(){

        //헤더에서 가져와야함.
        String email= "hyeri1126@google.com";
        presidentService.remove(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test10")
    ResponseEntity<String> test(HttpServletRequest request) {

        String email= request.getHeader("email");
        log.info("email: "+email);

        return ResponseEntity.ok(email);
    }
}
