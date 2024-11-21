package com.example.Attendance.dto;

import com.example.Attendance.model.Commute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommuteDailyResponse {
    private Integer commuteId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long commuteDuration;
    private Long commuteAmount;
    private Byte employeeType; //0이 시급 1이 월급

    public static CommuteDailyResponse from(Commute commute, Long commuteAmount) {
        return new CommuteDailyResponse(
                commute.getId(),
                commute.getStoreEmployee().getName(),
                commute.getStartTime(),
                commute.getEndTime(),
                commute.getCommuteDuration(),
                commuteAmount,
                commute.getStoreEmployee().getEmploymentType()
        );
    }
}
