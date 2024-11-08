package com.example.User.service;


import com.example.User.dto.login.ReqLoginData;
import com.example.User.dto.login.ReqRegist;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.repository.PresidentRepository;
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

    public boolean validateLogin(ReqLoginData reqLoginData) {
        President president =presidentRepository.findByEmail(reqLoginData.getEmail())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        return passwordEncoder.matches(reqLoginData.getPassword(), president.getPassword());
    }

    @Transactional
    public void regist(ReqRegist reqRegist) {

        boolean result= presidentRepository.existsByEmailAndPhoneNumber(
                reqRegist.getEmail(), reqRegist.getPhoneNumber());
        if(result)
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);

        reqRegist.setPassword(passwordEncoder.encode(reqRegist.getPassword()));
        President president= reqRegist.toEntity();
        presidentRepository.save(president);
    }

    @Transactional
    public void remove(String email){
        presidentRepository.deleteByEmail(email);
    }

    //사장 정보 수정
    @Transactional
    public void updatePresident(Integer id, String phoneNumber, LocalDate birthDate) {
        presidentRepository.updatePhoneNumberAndBirthDate(id,phoneNumber,birthDate);
    }


}
