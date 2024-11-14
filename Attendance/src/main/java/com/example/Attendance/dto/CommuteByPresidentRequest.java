package com.example.Attendance.dto;

import com.example.Attendance.model.Commute;
import com.example.Attendance.model.StoreEmployee;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommuteByPresidentRequest {

    @NotNull(message = "출근 시간은 필수입니다")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotNull(message = "출근일자는 필수입니다")
    @PastOrPresent(message = "미래 날짜는 입력할 수 없습니다")
    private LocalDate commuteDate;

    private CommuteByPresidentRequest(LocalDateTime startTime, LocalDateTime endTime, LocalDate commuteDate) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.commuteDate = commuteDate;
    }

    public Commute toEntity(StoreEmployee employee) {
        // endTime 유무에 따라 다른 생성 메서드 호출
        return endTime == null ?
                Commute.createCommuteCheckIn(commuteDate, startTime, employee) :
                Commute.createCompleteCommute(commuteDate, startTime, endTime, employee);
    }
}
