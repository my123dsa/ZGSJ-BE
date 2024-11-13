package com.example.User.controller;


import com.example.User.dto.login.ReqLoginData;
import com.example.User.dto.login.ReqRegist;
import com.example.User.dto.login.ResNewAccessToken;
import com.example.User.dto.presidentupdate.PresidentUpdateRequest;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;

import com.example.User.model.President;
import com.example.User.service.AuthService;
import com.example.User.service.PresidentService;
import com.example.User.service.RedisTokenService;
import com.example.User.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

        Integer id= presidentService.validateLogin(reqLoginData);

        String accessToken = authService.onAuthenticationSuccess(id);
        return ResponseEntity.ok(ResNewAccessToken.from(accessToken));
    }

    @GetMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request) {
        Integer id=Integer.parseInt(request.getHeader("id"));

        redisTokenService.removeRefreshToken(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    ResponseEntity<ResNewAccessToken> refresh(HttpServletRequest request) {
        String token=request.getHeader("Authorization").replace("Bearer ", "");

        Integer id= jwtUtil.getIdFromToken(token);
        log.info("id: "+id);

        String accessToken =redisTokenService.checkRefreshToken(id);

        return ResponseEntity.ok(ResNewAccessToken.from(accessToken));
    }

    @PostMapping("/regist")
    ResponseEntity<ResNewAccessToken> regist(@RequestBody ReqRegist reqRegist){
        log.info("reqRegist: "+reqRegist);
        Integer id= presidentService.regist(reqRegist);
        String accessToken =authService.onAuthenticationSuccess(id);
        return ResponseEntity.ok(ResNewAccessToken.from(accessToken));
    }

    @DeleteMapping("/secession")
    public ResponseEntity<Void> secession(HttpServletRequest request){
        Integer id=Integer.parseInt(request.getHeader("id"));
        presidentService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test10")
    ResponseEntity<Integer> test(HttpServletRequest request) {
        Integer id=Integer.parseInt(request.getHeader("id"));
        return ResponseEntity.ok(id);
    }


    //사장 정보수정 폰번호,생년월일
    @PutMapping("/modify")
    ResponseEntity<Void> updatePresident(@RequestBody PresidentUpdateRequest presidentUpdateRequest) {
        Integer id = 1;
        presidentService.updatePresident(id, presidentUpdateRequest.getPhoneNumber(),presidentUpdateRequest.getBirthDate());
        return ResponseEntity.ok().build();
    }



}


