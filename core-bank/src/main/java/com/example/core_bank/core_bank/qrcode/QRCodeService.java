package com.example.core_bank.core_bank.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
@Slf4j
public class QRCodeService {

    public String generateQRUrl(String data) {
        // 예시: http://localhost:8080/payment?key=abc123
        return "http://localhost:3030/hello";
    }

    public byte[] generateQRCodeImage(String data) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            //여기에 가게id붙여서 특정 폼 만들게 하는 qr 생성
            String url = generateQRUrl(data);  // URL 생성

            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("QR Code 생성 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException("QR Code 생성에 실패했습니다.", e);
        }
    }
    public String generateQRCodeBase64(String text) {
        byte[] qrCodeBytes = generateQRCodeImage(text);
        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }
}
