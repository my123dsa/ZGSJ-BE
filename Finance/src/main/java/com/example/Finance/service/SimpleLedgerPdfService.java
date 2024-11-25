package com.example.Finance.service;

import com.example.Finance.dto.TransactionHistoryWithCounterPartyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleLedgerPdfService extends PdfService {

    public byte[] generatecounterPartyPdf(List<TransactionHistoryWithCounterPartyResponse> transactions, boolean taxType) {
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
                .append("@page {")
                .append("    size: landscape;")
                .append("    margin: 18mm;")
                .append("}")
                .append("body {")
                .append("    font-family: 'NanumGothic';")
                .append("    margin: 0;")
                .append("    padding: 2px;")
                .append("    width: 100%;")
                .append("    display: flex;")
                .append("    justify-content: center;")
                .append("    align-items: center;")
                .append("}")
                .append("table {")
                .append("    width: 95%;")
                .append("    border-collapse: collapse;")
                .append("    margin-top: 20px;")
                .append("}")
                .append("th, td {")
                .append("    border: 1px solid black;")
                .append("    padding: 6px;")
                .append("    text-align: center;")
                .append("    font-size: 9pt;")
                .append("    font-family: 'NanumGothic';")
                .append("}")
                .append("th { background-color: #f2f2f2; }")
                .append(".amount { text-align: center; }")
                .append(".title {")
                .append("    text-align: center;")
                .append("    font-size: 24px;")
                .append("    margin-bottom: 20px;")
                .append("    font-family: 'NanumGothic';")
                .append("}")
                .append(".subheader { background-color: #e6e6e6; }")
                .append("th:nth-child(1), td:nth-child(1) {") // 날짜 열 너비 조정
                .append("    width: 120px;")
                .append("}")
                .append("th:nth-child(2), td:nth-child(2) {")
                .append("    width: 180px;")
                .append("}")
                .append("th:nth-child(3), td:nth-child(3) {")
                .append("    width: 120px;")
                .append("}")
                .append("th:nth-child(4), td:nth-child(4), th:nth-child(5), td:nth-child(5) {")
                .append("    width: 80px;")
                .append("}")
                .append("th:nth-child(6), td:nth-child(6), th:nth-child(7), td:nth-child(7) {")
                .append("    width: 80px;")
                .append("}")
                .append("th:nth-child(8), td:nth-child(8), th:nth-child(9), td:nth-child(9) {")
                .append("    width: 140px;")
                .append("}")
                .append("th:nth-child(10), td:nth-child(10) {")
                .append("    width: 150px;")
                .append("}")
                .append("</style>")
                .append("</head><body>")
                .append("<div class='title'>간편장부</div>");

        html.append("<table>")
                .append("<tr>")
                .append("<th rowspan='2'>날짜</th>")
                .append("<th rowspan='2'>거래내용</th>")
                .append("<th rowspan='2'>거래처</th>")
                .append("<th colspan='2'>수입</th>")
                .append("<th colspan='2'>비용</th>")
                .append("<th colspan='2'>고정자산 증감</th>")
                .append("<th rowspan='2'>비고</th>")
                .append("</tr>")
                .append("<tr class='subheader'>")
                .append("<th>금액</th>")
                .append("<th>부가세</th>")
                .append("<th>금액</th>")
                .append("<th>부가세</th>")
                .append("<th>금액</th>")
                .append("<th>부가세</th>")
                .append("</tr>");

        for (TransactionHistoryWithCounterPartyResponse transaction : transactions) {
            Integer amount = transaction.getAmount();
            BigDecimal vat = taxType && transaction.getIsDeposit() ?
                    new BigDecimal(amount).multiply(new BigDecimal("0.1")).setScale(0, RoundingMode.HALF_UP) :
                    BigDecimal.ZERO;

            html.append("<tr>")
                    .append("<td>").append(transaction.getTransactionDate()).append("</td>")
                    .append("<td>").append(escapeHtml(transaction.getIsDeposit() ? "매출" : transaction.getClassificationName())).append("</td>")
                    .append("<td>").append(escapeHtml(transaction.getCounterpartyName())).append("</td>");

            if (transaction.getIsDeposit()) {
                html.append("<td class='amount'>").append(formatNumber(amount)).append("</td>")
                        .append("<td class='amount'>").append(taxType ? formatNumber(vat.longValue()) : "").append("</td>")
                        .append("<td></td><td></td>")
                        .append("<td class='empty-cell'>&nbsp;</td><td class='empty-cell'>&nbsp;</td>")
                        .append("<td>")
                        .append(escapeHtml(transaction.getClassificationName()))
                        .append("(")
                        .append(formatNumber(amount))
                        .append(")")
                        .append("</td>");
            } else {
                html.append("<td></td><td></td>")
                        .append("<td class='amount'>").append(formatNumber(amount)).append("</td>")
                        .append("<td></td>")
                        .append("<td class='empty-cell'>&nbsp;</td><td class='empty-cell'>&nbsp;</td>")
                        .append("<td>계</td>");
            }
            html.append("</tr>");
        }

        //밑에 10 줄 만들기(직접 작성용)
        for (int i = 0; i < 10; i++) {
            html.append("<tr class='empty-row'>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("<td>&nbsp;</td>")
                    .append("</tr>");
        }

        html.append("</table></body></html>");

        return convertHtmlToPdf(html.toString());
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String formatNumber(long number) {
        return String.format("%,d", number);
    }
}