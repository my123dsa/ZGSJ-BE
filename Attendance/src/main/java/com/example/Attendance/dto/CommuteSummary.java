package com.example.Attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommuteSummary {
    private Integer employeeId;
    private Long commuteDuration;
}
