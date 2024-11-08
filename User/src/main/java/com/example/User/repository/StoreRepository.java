package com.example.User.repository;

import com.example.User.model.Store;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByStoreName(String storeName);

    List<Store> findAllByPresidentId(@Param("presidentId") Integer presidentId);

}
