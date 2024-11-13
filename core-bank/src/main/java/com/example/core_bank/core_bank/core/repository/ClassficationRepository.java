package com.example.core_bank.core_bank.core.repository;

import com.example.core_bank.core_bank.core.model.Classfication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassficationRepository extends JpaRepository<Classfication, Integer> {

    // classficationName으로 분류를 조회하는 메소드
    List<Classfication> findByClassficationName(String classficationName);
}
