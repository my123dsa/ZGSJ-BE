package com.example.User.controller;


import com.example.User.dto.login.ReqIdFindData;
import com.example.User.dto.login.ReqPwChange;
import com.example.User.dto.login.ResIdFindData;
import com.example.User.dto.presidentupdate.PresidentUpdateRequest;
import com.example.User.resolver.MasterId;
import com.example.User.service.PresidentService;
import jakarta.validation.Valid;
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

    @PostMapping("/findPassword") //사장님 아이디 찾기
    ResponseEntity<ResIdFindData> findId(@Valid @RequestBody ReqIdFindData reqIdFindData){
        String email = presidentService.findByNameAndPhoneNumber(reqIdFindData);
        return ResponseEntity.ok(ResIdFindData.from(email));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@MasterId Integer id,
                                               @Valid @RequestBody ReqPwChange reqpwChange) {
        presidentService.changePassword(id, reqpwChange);
        return ResponseEntity.ok().build();
    }


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


