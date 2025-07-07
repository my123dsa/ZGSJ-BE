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
    private static final String FONT_PATH = "/app/fonts/NanumGothic-Regular.ttf";

    public byte[] convertHtmlToPdf(String html) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver()
                    .addFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(baos, true);
            log.info("PDF 생성 완료: {} bytes", baos.size());
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("PDF 변환 중 오류 발생", e);
            throw new CustomException(ErrorCode.PDF_CREATE_ERROR);
        }
    }
}
