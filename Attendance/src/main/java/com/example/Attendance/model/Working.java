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
    private Employee employee;

    private Working(LocalDate workDay, LocalTime workDuration, LocalTime startTime, Employee employee) {
        this.workDay = workDay;
        this.workDuration = workDuration;
        this.startTime = startTime;
        this.employee = employee;
    }


    public static Working createWorking(LocalDate workDay, LocalTime workDuration, LocalTime startTime,Employee employee) {
        return new Working(workDay, workDuration, startTime, employee);
    }
}