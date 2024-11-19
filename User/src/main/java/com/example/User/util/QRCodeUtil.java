package com.example.User.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

//@Service
//@Slf4j
//public class QRCodeUtil {
//
//    public String generateQRUrl(Integer storeId) {
//        String ip= "localhost";
////        String server="";
//        return "http://" +ip+":8888/employee/"+storeId+"/commute";
//    }
//
//    public byte[] generateQRCodeImage(Integer storeId) {
//        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
//            //여기에 가게id붙여서 특정 폼 만들게 하는 qr 생성
//            String url = generateQRUrl(storeId);  // URL 생성
//
//            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
//            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
//
//            return outputStream.toByteArray();
//        } catch (Exception e) {
//            log.error("QR Code 생성 중 에러 발생: {}", e.getMessage());
//            throw new RuntimeException("QR Code 생성에 실패했습니다.", e);
//        }
//    }
//}
