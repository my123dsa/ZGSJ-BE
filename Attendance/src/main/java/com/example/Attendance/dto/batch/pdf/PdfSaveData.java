package com.example.Attendance.dto.batch.pdf;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PdfSaveData {
    private Integer id;
    private String url;

    public static PdfSaveData of(Integer id,String url){
        return new PdfSaveData(id,url);
    }
}
