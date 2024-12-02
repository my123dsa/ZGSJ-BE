package com.example.Attendance.service.batch;


import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
@Slf4j
public class PdfService {

    //그대로 사용하지 않고, Extends로 사용.

    public byte[] convertHtmlToPdf(String html) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ITextRenderer renderer = new ITextRenderer();

            // 폰트 설정
            ClassPathResource regular = new ClassPathResource("fonts/NanumGothic-Regular.ttf");
            renderer.getFontResolver().addFont(
                    regular.getFile().getAbsolutePath(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

            // HTML 설정
            renderer.setDocumentFromString(html);
            renderer.layout();

            // PDF 생성
            renderer.createPDF(baos, true);
            baos.flush();

            // 디버깅을 위한 파일 저장
            byte[] pdfContent = baos.toByteArray();

            log.info("PDF 생성 완료: {} bytes", pdfContent.length);
            return pdfContent;

        } catch (Exception e) {
            log.error("PDF 변환 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.PDF_CREATE_ERROR);
        }
    }
}