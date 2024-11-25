package com.example.Attendance.dto.batch.email;

import com.example.Attendance.model.PayStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailOutputData {

    private Integer batchId;
    private Boolean result;

    public static EmailOutputData of(Integer id, boolean result){
        return new EmailOutputData(id,result);
    }
}
