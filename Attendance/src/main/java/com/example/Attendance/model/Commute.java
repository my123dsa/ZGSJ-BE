package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "commute")
@Getter
@NoArgsConstructor
public class Commute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commute_id")
    private Integer id;

    @Column(name = "commute_date", nullable = false)
    private LocalDate commuteDate;

    @Column(name = "commute_duration")
    private Long commuteDuration;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "se_id")
    private StoreEmployee storeEmployee;

    private Commute(LocalDate commuteDate, LocalDateTime startTime, StoreEmployee storeEmployee) {
        this.commuteDate = commuteDate;
        this.commuteDuration = 0L;
        this.startTime = startTime;
        this.storeEmployee = storeEmployee;
    }

    //사장님이 출퇴근 모두 지정 용
    private Commute(LocalDate commuteDate, LocalDateTime startTime, LocalDateTime endTime, StoreEmployee storeEmployee) {
        this.commuteDate = commuteDate;
        this.startTime = startTime;
        this.endTime = endTime;
        calculateDuration(startTime, endTime);
        this.storeEmployee = storeEmployee;
    }


    public static Commute createCommuteCheckIn(LocalDate commuteDate, LocalDateTime startTime, StoreEmployee storeEmployee) {
        return new Commute(commuteDate, startTime, storeEmployee);
    }

    public static Commute createCompleteCommute(LocalDate commuteDate, LocalDateTime startTime, LocalDateTime endTime, StoreEmployee storeEmployee) {
        return new Commute(commuteDate, startTime, endTime, storeEmployee);
    }

    private void calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {
        this.commuteDuration = Duration.between(startTime, endTime).toMinutes();
    }

    public void setEndTime(LocalDateTime endTime) {
        calculateDuration(this.startTime, endTime);
        this.endTime = endTime;
    }
}