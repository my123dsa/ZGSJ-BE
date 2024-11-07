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
    @JoinColumn(name = "se_id")
    private StoreEmployee storeEmployee;

    private Working(LocalDate workDay, LocalTime startTime, StoreEmployee storeEmployee) {
        this.workDay = workDay;
        this.workDuration = LocalTime.of(0, 0);
        this.startTime = startTime;
        this.storeEmployee = storeEmployee;
    }

    public static Working createWorking(LocalDate workDay, LocalTime startTime, StoreEmployee storeEmployee) {
        return new Working(workDay, startTime, storeEmployee);
    }
}