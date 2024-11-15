package com.example.User.controller;


import com.example.User.dto.president.PresidentInfoResponse;
import com.example.User.dto.presidentupdate.PresidentUpdateRequest;
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
    public ResponseEntity<Void> secession(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getHeader("id"));
        presidentService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test10")
    public ResponseEntity<Integer> test(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getHeader("id"));
        return ResponseEntity.ok(id);
    }

    //사장 정보수정 폰번호,생년월일
    @PutMapping("/modify")
    public ResponseEntity<Void> updatePresident(@RequestBody PresidentUpdateRequest
                                                 presidentUpdateRequest, HttpServletRequest request) {
        Integer id = 1;
//        Integer id = Integer.parseInt(request.getHeader("id"));
        presidentService.updatePresident(id, presidentUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mypage")
    public ResponseEntity<PresidentInfoResponse> getPresidentInfo()
    {
        int presidentid= 1; //준현이맞춰서 변경 예정
        return ResponseEntity.ok(presidentService.getPresidentInfo(presidentid));
    }
}


