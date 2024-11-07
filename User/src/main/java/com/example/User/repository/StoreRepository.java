package com.example.User.repository;

import com.example.User.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByStoreName(String storeName);

    List<Store> findAllByPresidentId(Integer president_id);
}
