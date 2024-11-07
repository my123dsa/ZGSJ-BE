package com.example.core_bank.core_bank.core.repository;

import com.example.core_bank.core_bank.core.model.BusinessNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessNumberRepository extends CrudRepository<BusinessNumber, Integer> {
    boolean existsByBusinessNumber(String businessNumber);
}
