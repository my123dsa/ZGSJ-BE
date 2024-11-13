package com.example.User.service;


import com.example.User.dto.login.ReqIdFindData;
import com.example.User.dto.login.ReqLoginData;
import com.example.User.dto.login.ReqRegist;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.repository.PresidentRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class PresidentService {
    private final PresidentRepository presidentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Integer validateLogin(ReqLoginData reqLoginData) {
        President president =presidentRepository.findByEmail(reqLoginData.getEmail())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(reqLoginData.getPassword(), president.getPassword())){
            throw new CustomException(ErrorCode.PASSWORD_NOT_CORRECT);
        }
        return president.getId();
    }

    @Transactional
    public Integer regist(ReqRegist reqRegist) {

        boolean result= presidentRepository.existsByEmailAndPhoneNumber(
                reqRegist.getEmail(), reqRegist.getPhoneNumber());
        if(result)
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);

        reqRegist.setPassword(passwordEncoder.encode(reqRegist.getPassword()));
        President president= reqRegist.toEntity();
        presidentRepository.save(president);
        return president.getId();
    }

    @Transactional
    public void remove(Integer id){
        //확인 후 삭제하기
        presidentRepository.deleteById(id);
    }

    //사장 정보 수정
    @Transactional
    public void updatePresident(Integer id, String phoneNumber, LocalDate birthDate) {
        presidentRepository.updatePhoneNumberAndBirthDate(id,phoneNumber,birthDate);
    }

    //사장 아이디 찾기
    public String findByNameAndPhoneNumber(ReqIdFindData reqIdFindData){
        President president = presidentRepository.findByNameAndPhoneNumber(
                reqIdFindData.getName(),
                reqIdFindData.getPhoneNumber()
        ).orElseThrow(() -> new NotFoundException("일치하는 사용자 정보를 찾을 수 없습니다"));

        String presidentEmail = president.getEmail();

        return presidentEmail;
    }


}
