package com.example.User.controller;


import com.example.User.dto.login.*;
import com.example.User.dto.presidentupdate.PresidentUpdateRequest;
import com.example.User.service.AuthService;
import com.example.User.service.PresidentService;
import com.example.User.service.RedisTokenService;
import com.example.User.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    private final PresidentService presidentService;

    @PostMapping("/id-find") //사장님 아이디 찾기
    ResponseEntity<ResIdFindData> findId(@Valid @RequestBody ReqIdFindData reqIdFindData){
        String email = presidentService.findByNameAndPhoneNumber(reqIdFindData);
        return ResponseEntity.ok(ResIdFindData.from(email));
    }

    @DeleteMapping("/secession")
    public ResponseEntity<Void> secession(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getHeader("id"));
        presidentService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test10")
    ResponseEntity<Integer> test(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getHeader("id"));
        return ResponseEntity.ok(id);
    }

    //사장 정보수정 폰번호,생년월일
    @PutMapping("/modify")
    ResponseEntity<Void> updatePresident(@RequestBody PresidentUpdateRequest
                                                 presidentUpdateRequest, HttpServletRequest request) {
        Integer id = 1;
//        Integer id = Integer.parseInt(request.getHeader("id"));
        presidentService.updatePresident(id, presidentUpdateRequest);
        return ResponseEntity.ok().build();
    }
}


