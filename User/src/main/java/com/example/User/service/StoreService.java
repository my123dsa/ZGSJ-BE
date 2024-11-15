package com.example.User.service;

import com.example.User.dto.store.StoreRequest;
import com.example.User.dto.store.StoreResponse;
import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.model.President;
import com.example.User.model.Store;
import com.example.User.repository.PresidentRepository;
import com.example.User.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final PresidentRepository presidentRepository;

    @Transactional
    public List<Object> registerStore(Integer presidentId,StoreRequest storeRequest) {
        President president = presidentRepository.findById(presidentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRESIDENT_NOT_FOUND));
        boolean isStoreNameExists = storeRepository.existsByStoreName(storeRequest.getStoreName());
        if (isStoreNameExists) {
            throw new CustomException(ErrorCode.DUPLICATE_STORE_NAME);
        }
        Store store = storeRequest.toEntity(president);
        //mapService.getCoordinates(storeRequest.getLocation());// 저장하고
        Integer id = storeRepository.save(store).getId();
        List<Object> idAndEmail = new ArrayList<>();
        idAndEmail.add(id);
        idAndEmail.add(president.getEmail());
        return idAndEmail;
    }

    public List<StoreResponse> showStores(Integer presidentId) {
        // President 존재 여부 확인
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

        existedStore.updateByStoreRequest(storeRequest);

        storeRepository.save(existedStore);
    }

    @Transactional
    public void deleteStore(Integer storeId) {
        storeRepository.deleteById(storeId);
    }
}
