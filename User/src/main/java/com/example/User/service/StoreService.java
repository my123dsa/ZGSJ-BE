package com.example.User.service;

import com.example.User.dto.store.StoreRequest;
import com.example.User.dto.store.StoreResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.model.Store;
import com.example.User.model.StoreEmployee;
import com.example.User.repository.PresidentRepository;
import com.example.User.repository.StoreRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final PresidentRepository presidentRepository;

    public StoreService(StoreRepository storeRepository, PresidentRepository presidentRepository) {
        this.storeRepository = storeRepository;
        this.presidentRepository = presidentRepository;
    }

    @Transactional
    public void registerStore(StoreRequest storeRequest) {
        Integer presidentId = 1;
        President president = presidentRepository.findById(presidentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRESIDENT_NOT_FOUND));

        boolean isStoreNameExists = storeRepository.existsByStoreName(storeRequest.getStoreName());
        if (isStoreNameExists) {
            throw new CustomException(ErrorCode.DUPLICATE_STORE_NAME);
        }

        Store store = storeRequest.toEntity(president);
        storeRepository.save(store);
    }

    public List<StoreResponse> showStores() {
        // President 존재 여부 확인
        Integer presidentId = 1;
        List<Store> stores = storeRepository.findAllByPresidentId(presidentId);
        return stores.stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStore(Integer storeId, StoreRequest storeRequest) {
        // 기존 store 조회
        Store existedStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        existedStore.updateStore(storeRequest);

        storeRepository.save(existedStore);
    }

    @Transactional
    public void deleteStore(Integer storeId) {
        storeRepository.deleteById(storeId);
    }
}
