package com.example.User.service;

import com.example.User.error.CustomException;
import com.example.User.repository.StoreEmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = "spring.mail.enabled=false")
class StoreEmployeeServiceTest {
    @Autowired
    StoreEmployeeService storeEmployeeService;

    @Autowired
    StoreEmployeeRepository storeEmployeeRepository;

    @Test
    @DisplayName("삭제 시 계좌 정보 변경 확인")
    public void setStoreEmployeeService() {
        Integer realid = 4 ;

        storeEmployeeService.deleteEmployee(4);

        Assertions.assertEquals(storeEmployeeRepository.findById(4).get().getAccountNumber(), "탈퇴하여 계좌 정보를 가져올 수 없습니다.");
    }

    @Test
    @DisplayName("없는 사용자")
    public void setStoreEmployeeServiceTest() {
        assertThrows(CustomException.class, () -> {
            storeEmployeeService.deleteEmployee(-1);
        }, "존재하지 않는 직원 삭제 시 CustomException이 발생해야 합니다");
    }
}
