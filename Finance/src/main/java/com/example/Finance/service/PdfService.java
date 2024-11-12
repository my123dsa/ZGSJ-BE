package com.example.Finance.service;

import com.example.Finance.dto.IncomeStatementResponse;
import com.example.Finance.dto.TransactionHistoryResponse;
import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class PdfService {

    public byte[] generateIncomeStatementPdf(List<TransactionHistoryResponse> transactionHistoryResponseList,
                                             IncomeStatementResponse incomeStatementResponse) {
        // HTML 생성
        String html = generateHtml(incomeStatementResponse);
        log.info("Generated HTML: {}", html);  // HTML 로깅 추가

        // HTML을 PDF로 변환
        return convertHtmlToPdf(html);
    }

    private String generateHtml(IncomeStatementResponse incomeStatementResponse) {
        StringBuilder html = new StringBuilder();
        html.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">")
                .append("<html xmlns=\"http://www.w3.org/1999/xhtml\">")
                .append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>")
                .append("<style type=\"text/css\">")
                .append("@font-face {")
                .append("    font-family: 'NanumGothic';")
                .append("    src: url('classpath:/fonts/NanumGothic-Regular.ttf') format('truetype');")
                .append("}")
                .append("body { font-family: 'NanumGothic', Arial, sans-serif; margin: 40px; }")
                .append(".title { text-align: center; font-size: 24px; margin-bottom: 30px; font-weight: bold; }")
                .append(".section { margin: 20px 0; }")
                .append(".label { font-size: 14px; font-weight: bold; }")
                .append(".value { text-align: right; font-size: 12px; margin: 5px 0 15px 0; }")
                .append(".divider { border-top: 1px solid #ccc; margin: 10px 0; }")
                .append("</style>")
                .append("</head><body>");

        // 제목
        html.append("<div class=\"title\">손익 계산서</div>");

        if (incomeStatementResponse != null) {
            // 총 매출액
            addHtmlSection(html, "총 매출액", incomeStatementResponse.getTotalRevenue());
            html.append("<div class=\"divider\"></div>");

            // 매출원가
            addHtmlSection(html, "매출원가", incomeStatementResponse.getCostOfSales());
            html.append("<div class=\"divider\"></div>");

            // 매출총이익
            addHtmlSection(html, "매출총이익", incomeStatementResponse.getGrossProfit());
            html.append("<div class=\"divider\"></div>");

            // 판매관리비
            addHtmlSection(html, "판매관리비", incomeStatementResponse.getOperatingExpenses());
            html.append("<div class=\"divider\"></div>");

            // 영업이익
            addHtmlSection(html, "영업이익", incomeStatementResponse.getOperatingIncome());
            html.append("<div class=\"divider\"></div>");

            // 수익률
            addHtmlSectionPercent(html, "수익률", incomeStatementResponse.getProfitMargin());
        }

        html.append("</body></html>");
        return html.toString();
    }

    private void addHtmlSection(StringBuilder html, String label, BigDecimal value) {
        String valueStr = (value != null) ? String.format("%,d원", value.longValue()) : "0원";
        html.append("<div class=\"section\">")
                .append("<div class=\"label\">").append(label).append("</div>")
                .append("<div class=\"value\">").append(valueStr).append("</div>")
                .append("</div>");
    }

    private void addHtmlSectionPercent(StringBuilder html, String label, BigDecimal value) {
        String valueStr = (value != null) ? String.format("%,d%%", value.longValue()) : "0%";
        html.append("<div class=\"section\">")
                .append("<div class=\"label\">").append(label).append("</div>")
                .append("<div class=\"value\">").append(valueStr).append("</div>")
                .append("</div>");
    }

    private byte[] convertHtmlToPdf(String html) {
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