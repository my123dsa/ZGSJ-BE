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

    private Integer seId;
    private Integer batchId;
    private Boolean result;
    private Boolean isMask;

    public static EmailOutputData of(Integer seId,Integer batchId, Boolean result,Boolean isMask){
        return new EmailOutputData(seId,batchId,result,isMask);
    }
}
