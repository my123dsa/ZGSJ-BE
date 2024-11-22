package com.example.Attendance.dto.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommuteSummary {
    private Integer employeeId;
    private Long commuteDuration;
    private Integer dayLength;

    public CommuteSummary updateDuration() {
        double tempt = (double) this.commuteDuration / 60;
        if (tempt % 1 > 0.83203125) {
            this.commuteDuration = (long) (Math.floor(tempt) + 1);  // 정수 부분에 1을 더함
        } else
            this.commuteDuration = (long) Math.floor(tempt);
        return this;
    }
}
