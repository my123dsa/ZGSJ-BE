package com.example.core_bank.core_bank.core.repository;

import com.example.core_bank.core_bank.core.model.Account;
import com.example.core_bank.core_bank.core.model.Bank;
import com.example.core_bank.core_bank.core.model.Classfication;
import com.example.core_bank.core_bank.core.model.TransactionHistory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/banking",
        "spring.datasource.username=root",
        "spring.datasource.password=1234",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.show-sql=true",
        "spring.jpa.properties.hibernate.format_sql=true"
})
class TransactionHistoryRepositoryTest {

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClassficationRepository classficationRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    @DisplayName("거래내역 년, 월별 조회 쿼리 테스트")
    void findByAccountIdAndYearAndMonthWithClassfication() {
        // 실제 DB에서 Bank 조회
        Bank bank = em.createQuery("SELECT b FROM Bank b WHERE b.bankCode = :bankCode", Bank.class)
                .setParameter("bankCode", "004")
                .getSingleResult();

        Account account = Account.createAccount(
                "1234567890",
                "테스트계좌",
                bank  // DB에서 조회한 Bank 사용
        );
        accountRepository.save(account);

        Classfication classification = Classfication.createClassfication("식비");
        classficationRepository.save(classification);


        transactionHistoryRepository.save(new TransactionHistory(
                LocalDate.of(2024, 11, 15),
                50000L,
                false,
                "출금",
                "거래처2",
                account,
                classification
        ));

        // 2024년 11월 데이터 (조회되어야 함)
        transactionHistoryRepository.save(new TransactionHistory(
                LocalDate.of(2024, 11, 1),
                100000L,
                true,
                "입금",
                "거래처1",
                account,
                classification
        ));


        // 다른 날짜 데이터 (조회되지 않아야 함)
        transactionHistoryRepository.save(new TransactionHistory(
                LocalDate.of(2024, 10, 1),
                30000L,
                true,
                "입금",
                "거래처3",
                account,
                classification
        ));

        List<TransactionHistory> result = transactionHistoryRepository
                .findByAccountIdAndYearAndMonthWithClassfication(account.getId(), 2024, 11);

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result).allMatch(transaction ->
                        transaction.getTransactionDate().getYear() == 2024 &&
                                transaction.getTransactionDate().getMonthValue() == 11 &&
                                transaction.getAccount().getId().equals(account.getId())
                ),
                () -> assertThat(result).isSortedAccordingTo(
                        Comparator.comparing(TransactionHistory::getTransactionDate)
                )
        );
    }


    @Test
    @Transactional
    @DisplayName("거래내역 연간 조회")
    void findByAccountIdAndYearWithClassfication() {
        // 실제 DB에서 Bank 조회
        Bank bank = em.createQuery("SELECT b FROM Bank b WHERE b.bankCode = :bankCode", Bank.class)
                .setParameter("bankCode", "004")
                .getSingleResult();

        Account account = Account.createAccount(
                "1234567890",
                "테스트계좌",
                bank  // DB에서 조회한 Bank 사용
        );
        accountRepository.save(account);

        Classfication classification = Classfication.createClassfication("식비");
        classficationRepository.save(classification);


        transactionHistoryRepository.save(new TransactionHistory(
                LocalDate.of(2024, 11, 15),
                50000L,
                true,
                "입금",
                "거래처2",
                account,
                classification
        ));

        // 2024년 11월 데이터 (조회되어야 함)
        transactionHistoryRepository.save(new TransactionHistory(
                LocalDate.of(2024, 11, 1),
                100000L,
                true,
                "입금",
                "거래처1",
                account,
                classification
        ));


        // 다른 날짜 데이터 (조회되지 않아야 함)
        transactionHistoryRepository.save(new TransactionHistory(
                LocalDate.of(2022, 10, 1),
                30000L,
                true,
                "입금",
                "거래처3",
                account,
                classification
        ));

        List<TransactionHistory> result = transactionHistoryRepository
                .findByAccountIdYearlyWithClassfication(account.getId(), 2024);

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result).allMatch(transaction ->
                        transaction.getTransactionDate().getYear() == 2024 &&
                                transaction.getAccount().getId().equals(account.getId())
                ),
                () -> assertThat(result).isSortedAccordingTo(
                        Comparator.comparing(TransactionHistory::getTransactionDate)
                )
        );
    }
}