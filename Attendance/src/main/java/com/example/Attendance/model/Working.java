package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "working")
@Getter
@NoArgsConstructor
public class Working {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "working_id")
    private Integer id;

    @Column(name = "work_day", nullable = false)
    private LocalDate workDay;

    @Column(name = "work_duration")
    private LocalTime workDuration;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeSub employeeSub;

    private Working(LocalDate workDay, LocalTime startTime, EmployeeSub employeeSub) {
        this.workDay = workDay;
        this.startTime = startTime;
        this.employeeSub = employeeSub;
    }

    public static Working createWorking(LocalDate workDay, LocalTime startTime, EmployeeSub employeeSub){
     return new Working(workDay, startTime, employeeSub);
    }
}