package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "working")
@Getter
@Setter
public class Working {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "working_id")
    private Long id;

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
    private employee_sub employee;
}