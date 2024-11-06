package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;


import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "working")
@Getter

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
    private EmployeeSub employee;

    private Working(LocalDate workDay, LocalTime startTime, LocalTime endTime, EmployeeSub employee) {
        this.workDay = workDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employee = employee;
    }

    public static Working createWorking(LocalDate workDay, LocalTime startTime, LocalTime endTime, EmployeeSub employee){
     return new Working(workDay, startTime, endTime, employee);
    }
}