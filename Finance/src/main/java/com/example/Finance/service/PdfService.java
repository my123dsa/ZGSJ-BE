package com.example.Finance.service;

import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class PdfService {

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
            savePdfToFile(pdfContent);  // 디버깅용

            log.info("PDF 생성 완료: {} bytes", pdfContent.length);
            return pdfContent;

        } catch (Exception e) {
            log.error("PDF 변환 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("PDF 생성 실패", e);
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                log.error("스트림 닫기 실패", e);
            }
        }
    }

    // 디버깅용 메서드
    private void savePdfToFile(byte[] pdfContent) {
        try {
            java.nio.file.Files.write(
                    java.nio.file.Paths.get("debug_output.pdf"),
                    pdfContent
            );
            log.info("디버그용 PDF 파일 저장 완료");
        } catch (IOException e) {
            log.error("PDF 파일 저장 실패", e);
        }
    }
}