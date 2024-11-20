package com.example.Attendance.repository;

import com.example.Attendance.dto.EmployeeNameResponse;
import com.example.Attendance.dto.BatchInputData;
import com.example.Attendance.model.StoreEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreEmployeeRepository extends JpaRepository<StoreEmployee, Integer> {

    //이것도 필요한거 dto로 보내게끔
    @Query("SELECT new com.example.Attendance.dto.EmployeeNameResponse(se.id, se.name) " +
            "FROM StoreEmployee se " +
            "WHERE se.store.id = :storeId")
    List<EmployeeNameResponse> findSimpleInfoByStoreId(@Param("storeId") Integer storeId);

    @Query("select se from StoreEmployee se join fetch se.store s  where se.email= :email and s.id= :storeId")
    Optional<StoreEmployee> findByEmailAndStoreId
            (@Param("email") String email, @Param("storeId") Integer storeId);

//    @Query("select se from StoreEmployee se join fetch se.store s join fetch s.president p where se.paymentDate= :paymentDate")
//    List<StoreEmployee> findAllByPaymentDate(Integer paymentDate);

    @Query("SELECT new com.example.Attendance.dto.BatchInputData(" +
            "se.id, s.accountNumber,'020',se.employmentType, se.accountNumber, se.bankCode, " +
            "se.salary, se.name, p.name) " +
            "FROM StoreEmployee se " +
            "JOIN se.store s " +
            "JOIN s.president p " +
            "WHERE se.paymentDate = :paymentDate")
    List<BatchInputData> findAllBatchInputDataByPaymentDate(@Param("paymentDate") Integer paymentDate);
}
