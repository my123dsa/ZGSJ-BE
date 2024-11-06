package com.example.User.controller;

import com.example.User.dto.businessnumber.BusinessNumberRequest;
import com.example.User.dto.businessnumber.BusinessNumberResponse;
import com.example.User.service.ValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ValidationController.class)
@ExtendWith(MockitoExtension.class)
class ValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidationService validationService;

    @Test
    @DisplayName("사업자번호 검증 성공 테스트")
    void validateBusinessNumber_Success() throws Exception {

        BusinessNumberResponse response = new BusinessNumberResponse(true, false, "등록된 사업자번호입니다.");

        when(validationService.validateBusinessNumber(any(BusinessNumberRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/businesscheck")    // post() 메서드 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"businessNumber\":\"1111111111\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사업자번호 검증 중 내부 오류")
    void validateBusinessNumber_error() throws Exception {

        BusinessNumberResponse response = new BusinessNumberResponse(false, true, "오류발생");

        when(validationService.validateBusinessNumber(any(BusinessNumberRequest.class)))
                .thenReturn(response);

        MvcResult result = mockMvc.perform(post("/businesscheck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"businessNumber\":\"1111111111\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("오류발생");
    }

}