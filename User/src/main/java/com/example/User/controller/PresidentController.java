package com.example.User.controller;


import com.example.User.dto.presidentupdate.PresidentUpdateRequest;
import com.example.User.resolver.MasterId;
import com.example.User.service.PresidentService;
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
    private final PresidentService presidentService;

    @DeleteMapping("/secession")
    public ResponseEntity<Void> secession(@MasterId Integer id) {
        presidentService.remove(id);
        return ResponseEntity.ok().build();
    }

    //사장 정보수정 폰번호,생년월일
    @PutMapping("/modify")
    ResponseEntity<Void> updatePresident(@MasterId Integer id,@RequestBody PresidentUpdateRequest
                                                 presidentUpdateRequest) {
        presidentService.updatePresident(id, presidentUpdateRequest);
        return ResponseEntity.ok().build();
    }
}


