package com.example.User.repository;

import com.example.User.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    boolean existsByStoreName(String storeName);

    List<Store> findAllByPresidentId(Integer presidentId);

    @Query("SELECT s FROM Store s WHERE s.id = :id AND NOT EXISTS " +
            "(SELECT 1 FROM Store st WHERE st.storeName = :storeName)")
    Optional<Store> findById(@Param("id") Integer id,@Param("storeName") String storeName);
    // select * from store s where s.id= :id and not exists storeName: s


    boolean existsByStoreNameAndIdNot(String storeName, Integer id);

}
