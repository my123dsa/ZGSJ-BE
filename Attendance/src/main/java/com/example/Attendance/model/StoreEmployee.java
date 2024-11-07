package com.example.Attendance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "store_employee",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"store_id", "employee_id"}
                )
        }
)
@Getter
@NoArgsConstructor
public class StoreEmployee {
    @Id
    @Column(name = "se_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "salary", nullable = false)
    private Long salary;

    @Column(name = "employment_type", nullable = false)
    private Boolean employmentType;

    @Column(name = "bank_code", length = 50, nullable = false)
    private String bankCode;

    @Column(name = "account_number", length = 50, nullable = false)
    private String accountNumber;

    @Column(name = "payment_date", nullable = false)
    @Min(1)
    @Max(28)
    private Integer paymentDate;
}
