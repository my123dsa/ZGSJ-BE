package com.example.User.service;

import com.example.User.dto.store.StoreRequest;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.model.Store;
import com.example.User.repository.PresidentRepository;
import com.example.User.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private PresidentRepository presidentRepository;

    @InjectMocks
    private StoreService storeService;

    @Test
    @DisplayName("가게 등록 성공 테스트")
    void registerStore_Success() {
        // Given
        Integer presidentId = 1;
        String storeEmail = "test@test.com";

        StoreRequest storeRequest = new StoreRequest(1,
                "테스트 가게",     // storeName
                "서울시 강남구",    // location
                "가게 설명",       // description
                "010-1234-5678",  // contact,
                "위치",
                123.5125,
                123.51234
        );

        President president = President.createPresident(
                "홍길동",
                "서울시 강남구",
                storeEmail,
                "password123",
                LocalDate.of(1990, 1, 1),
                "01012345678",
                true
        );

        Store store = storeRequest.toEntity(president);


        Store savedStore = Store.createStore(
                store.getStoreName(),
                store.getBusinessNumber(),
                store.getAccountNumber(),
                store.getBankCode(),
                president,
                "위치",
                123.5125,
                123.51234
        );


        when(presidentRepository.findById(presidentId)).thenReturn(Optional.of(president));
        when(storeRepository.existsByStoreName(storeRequest.getStoreName())).thenReturn(false);
        when(storeRepository.save(any(Store.class))).thenReturn(savedStore);

        // When
        List<Object> result = storeService.registerStore(presidentId, storeRequest);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(savedStore.getId());
        assertThat(result.get(1)).isEqualTo(storeEmail);

        verify(presidentRepository).findById(presidentId);
        verify(storeRepository).existsByStoreName(storeRequest.getStoreName());
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    @DisplayName("존재하지 않는 사장님 ID로 가게 등록 시 예외 발생")
    void registerStore_PresidentNotFound() {
        // Given
        Integer presidentId = 999;
        StoreRequest storeRequest = new StoreRequest(1,
                "테스트 가게",
                "서울시 강남구",
                "가게 설명",
                "010-1234-5678",
                "위치",
                123.5125,
                123.51234
        );

        when(presidentRepository.findById(presidentId)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                storeService.registerStore(presidentId, storeRequest)
        );

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PRESIDENT_NOT_FOUND);
    }

    @Test
    @DisplayName("중복된 가게 이름으로 등록 시 예외 발생")
    void registerStore_DuplicateStoreName() {
        // Given
        Integer presidentId = 1;
        StoreRequest storeRequest = new StoreRequest(1,
                "중복된 가게 이름",
                "서울시 강남구",
                "가게 설명",
                "010-1234-5678",
                "위치",
                123.5125,
                123.51234
        );

        President president = President.createPresident(
                "홍길동",
                "서울시 강남구",
                "test@test.com",
                "password123",
                LocalDate.of(1990, 1, 1),
                "01012345678",
                true
        );

        when(presidentRepository.findById(presidentId)).thenReturn(Optional.of(president));
        when(storeRepository.existsByStoreName(storeRequest.getStoreName())).thenReturn(true);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () ->
                storeService.registerStore(presidentId, storeRequest)
        );

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_STORE_NAME);
    }
}