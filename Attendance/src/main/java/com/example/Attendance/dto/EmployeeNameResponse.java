package com.example.Attendance.dto;

import com.example.Attendance.model.StoreEmployee;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeNameResponse {
    private Integer storeemployeeId;  // se_id를 더 명확한 이름으로
    private String name;

    public static EmployeeNameResponse from(StoreEmployee employee) {
        return new EmployeeNameResponse(
                employee.getId(),
                employee.getName()
        );
    }
}
