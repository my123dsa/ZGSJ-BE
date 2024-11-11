package com.example.Attendance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalTime commuteDuration;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "se_id")
    private StoreEmployee storeEmployee;

    private Commute(LocalDate commuteDate, LocalTime startTime, StoreEmployee storeEmployee) {
        this.commuteDate = commuteDate;
        this.commuteDuration = LocalTime.of(0, 0);
        this.startTime = startTime;
        this.storeEmployee = storeEmployee;
    }

    //사장님이 출퇴근 모두 지정 용
    private Commute(LocalDate commuteDate, LocalTime startTime, LocalTime endTime, StoreEmployee storeEmployee) {
        this.commuteDate = commuteDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.commuteDuration = calculateDuration(startTime, endTime);
        this.storeEmployee = storeEmployee;
    }


    public static Commute createCommuteCheckIn(LocalDate commuteDate, LocalTime startTime, StoreEmployee storeEmployee) {
        return new Commute(commuteDate, startTime, storeEmployee);
    }

    public static Commute createCompleteCommute(LocalDate commuteDate, LocalTime startTime, LocalTime endTime, StoreEmployee storeEmployee) {
        return new Commute(commuteDate, startTime, endTime, storeEmployee);
    }

    private LocalTime calculateDuration(LocalTime startTime, LocalTime endTime) {
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        long minutes = ChronoUnit.MINUTES.between(startTime, endTime) % 60;
        return LocalTime.of((int) hours, (int) minutes);
    }
}