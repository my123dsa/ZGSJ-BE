package com.example.Attendance.repository;

import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "spring.datasource.url=jdbc:mysql://localhost:3306/attendance?useSSL=false&serverTimezone=UTC",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.username=root",
        "spring.datasource.password=1234",
        "spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect"
})
class PayStatementRepositoryTest {

    @Autowired
    private PayStatementRepository payStatementRepository;

    @Test
    @DisplayName("급여 명세서 확인 url")
    public void getPayStatementUrl()
    {
        String answer = payStatementRepository.findPayStatementURL(1).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_PAY_STATEMENT)
        );
        assertEquals(answer,"https://example.com/pay_statement_1");
    }

    @Test
    @DisplayName("급여 명세서 확인 url 실패")
    public void getPayStatementUrlFail() {
        assertThrows(CustomException.class, () -> {
            payStatementRepository.findPayStatementURL(-1).orElseThrow(
                    () -> new CustomException(ErrorCode.INVALID_PAY_STATEMENT)
            );
        });
    }

}