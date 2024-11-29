package com.example.User.service;

import com.example.User.dto.manager.ManagerResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.repository.ManagerRepository;
import com.example.User.repository.PresidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final PresidentRepository presidentRepository;

    public List<ManagerResponse> getAllPresidents() {
        List<President> presidents = managerRepository.findAll();

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
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // President 삭제
        presidentRepository.deleteById(presidentId);
    }
}
