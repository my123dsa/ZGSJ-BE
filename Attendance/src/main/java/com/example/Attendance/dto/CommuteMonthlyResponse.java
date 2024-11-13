package com.example.Attendance.dto;

import com.example.Attendance.model.Commute;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommuteMonthlyResponse {
    private LocalDate commuteDate;
    private String name;
    private CommuteStatus status;

    @Getter
    @AllArgsConstructor
    public enum CommuteStatus {
        COMPLETED("퇴근완료"),
        IN_PROGRESS("출근중"),
        INCOMPLETE("미퇴근");

        private final String description;
    }

    //backend logic :
    //1. 출근을 했고, 퇴근이 찍혀있다면. 회색
    //2. 출근을 했고, 오늘 이전날짜에 출근했고, 퇴근이 없다면 퇴근 안찍은거
    //3. 출근을 했고, 오늘 출근했고, 퇴근이 없다면 출근중
    public static CommuteMonthlyResponse from(Commute commute) {
        return new CommuteMonthlyResponse(
                commute.getCommuteDate(),
                commute.getStoreEmployee().getName(),
                determineStatus(commute)
        );
    }

    private static CommuteStatus determineStatus(Commute commute) {
        if (commute.getEndTime() != null) {
            return CommuteStatus.COMPLETED;
        }

        return commute.getCommuteDate().equals(LocalDate.now()) ?
                CommuteStatus.IN_PROGRESS :
                CommuteStatus.INCOMPLETE;
    }

}
