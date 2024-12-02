package com.example.User.service;

import com.example.User.dto.manager.ManagerResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.repository.PresidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final PresidentRepository presidentRepository;

    public List<ManagerResponse> getAllPresidents() {
        List<President> presidents = presidentRepository.findAll();

        // President -> ManagerResponse 변환
        return presidents.stream()
                .map(president -> new ManagerResponse(
                        president.getId(),
                        president.getName(),
                        president.getEmail(),
                        president.getPhoneNumber()
                ))
                .collect(Collectors.toList());
    }
    // President 삭제
    public void deletePresidentById(Integer presidentId) {
        // 삭제 전 해당 ID의 President가 존재하는지 확인
        boolean exists = presidentRepository.existsById(presidentId);
        if (!exists) {
            throw new CustomException(ErrorCode.PRESIDENT_NOT_FOUND);
        }

        // President 삭제
        presidentRepository.deleteById(presidentId);
    }

    // 관리자 이메일 확인
    public void checkmanager(String email) {

        String ADMIN_EMAIL = "zipgyesajang@gmail.com";

        if (!email.equals(ADMIN_EMAIL)) {
            // 일치하지 않으면 예외 던짐
            throw new CustomException(ErrorCode.ADMIN_NOT_FOUND);
        }
    }

    // id로 이메일을 가져오는 메소드
    public String getEmailById(Integer id) {
        President president = presidentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRESIDENT_NOT_FOUND));
        return president.getEmail();
    }
}

