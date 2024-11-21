package com.example.core_bank.core_bank.service;

import com.example.core_bank.core_bank.core.dto.transfer.TransferRequest;
import com.example.core_bank.core_bank.core.model.Account;
import com.example.core_bank.core_bank.core.model.Bank;
import com.example.core_bank.core_bank.core.repository.AccountRepository;
import com.example.core_bank.core_bank.core.service.BankCoreService;
import com.example.core_bank.core_bank.global.error.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankCoreServiceTest {


    @Mock
    private AccountRepository accountRepository;


    @Spy
    @InjectMocks
    private BankCoreService bankCoreService;

    private TransferRequest transferRequest;
    private Account fromAccount;
    private Account toAccount;
    private Bank fromBank;
    private Bank toBank;
    @BeforeEach
    void setUp() {
        // Bank 설정
        fromBank = Bank.createBank("001", "FromBank");
        toBank = Bank.createBank("002", "ToBank");
        ReflectionTestUtils.setField(fromBank, "id", 1);
        ReflectionTestUtils.setField(toBank, "id", 2);

        // Account 설정
        fromAccount = Account.createAccountWithBalance("1234", "John", fromBank, 2000L);
        toAccount = Account.createAccountWithBalance("5678", "Jane", toBank, 1000L);
        ReflectionTestUtils.setField(fromAccount, "id", 1);
        ReflectionTestUtils.setField(toAccount, "id", 2);

        // TransferRequest 설정
        transferRequest = new TransferRequest();
        ReflectionTestUtils.setField(transferRequest, "fromAccount", "1234");
        ReflectionTestUtils.setField(transferRequest, "fromBankCode", "001");
        ReflectionTestUtils.setField(transferRequest, "fromAccountDepositor", "John");
        ReflectionTestUtils.setField(transferRequest, "toAccount", "5678");
        ReflectionTestUtils.setField(transferRequest, "toBankCode", "002");
        ReflectionTestUtils.setField(transferRequest, "toAccountDepositor", "Jane");
        ReflectionTestUtils.setField(transferRequest, "amount", 1000L);
    }

    @Test
    @DisplayName("정상적으로 이체 완료 되었을 경우")
    public void transferSuccess() {
        // given
        given(accountRepository.findByAccountNumberAndBankCodeAndName(
                transferRequest.getFromAccount(),
                transferRequest.getFromBankCode(),
                transferRequest.getFromAccountDepositor()))
                .willReturn(Optional.of(fromAccount));

        given(accountRepository.findByAccountNumberAndBankCodeAndName(
                transferRequest.getToAccount(),
                transferRequest.getToBankCode(),
                transferRequest.getToAccountDepositor()))
                .willReturn(Optional.of(toAccount));

        doNothing().when(bankCoreService).createHistories(any(), any(), any(), any());
        // when
        LocalDate result = bankCoreService.transfer(transferRequest);

        // then
        assertNotNull(result);

        verify(accountRepository).updateBalances(
                fromAccount.getId(),
                toAccount.getId(),
                1000L,
                2000L
        );
    }

    @Test
    @DisplayName("잔액보다 출금 금액이 더 클 경우")
    public void throwExceptionWhenInsufficientBalance() {
        // given
        ReflectionTestUtils.setField(transferRequest, "amount", 3000L);

        given(accountRepository.findByAccountNumberAndBankCodeAndName(
                transferRequest.getFromAccount(),
                transferRequest.getFromBankCode(),
                transferRequest.getFromAccountDepositor()))
                .willReturn(Optional.of(fromAccount));

        given(accountRepository.findByAccountNumberAndBankCodeAndName(
                transferRequest.getToAccount(),
                transferRequest.getToBankCode(),
                transferRequest.getToAccountDepositor()))
                .willReturn(Optional.of(toAccount));

        // when & then
        assertThrows(CustomException.class, () ->
                bankCoreService.transfer(transferRequest)
        );

        verify(accountRepository, never()).updateBalances(any(), any(), any(), any());
    }

    @Test
    @DisplayName("계좌 못찾았을 경우")
    public void throwExceptionWhenAccountNotFound() {
        // given
        given(accountRepository.findByAccountNumberAndBankCodeAndName(
                transferRequest.getFromAccount(),
                transferRequest.getFromBankCode(),
                transferRequest.getFromAccountDepositor()))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () ->
                bankCoreService.transfer(transferRequest)
        );

        verify(accountRepository, never()).updateBalances(any(), any(), any(), any());
    }
}
