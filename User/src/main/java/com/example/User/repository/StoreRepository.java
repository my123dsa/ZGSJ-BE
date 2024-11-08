package com.example.User.repository;

import com.example.User.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByStoreName(String storeName);

    List<Store> findAllByPresidentId(Integer presidentId);

}
