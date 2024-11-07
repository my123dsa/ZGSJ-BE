package com.example.core_bank.core_bank.qrcode;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QRCodeController {
    private final QRCodeService qrCodeService;

    @GetMapping("/hello")
    public String hello(){
        log.info("hello");
        return "hello";
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateQRCode() {
        byte[] qrCodeImage = qrCodeService.generateQRCodeImage("text");

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeImage);
    }
    @GetMapping("/generate/base64")
    public ResponseEntity<String> generateQRCodeBase64() {
        String base64QRCode = qrCodeService.generateQRCodeBase64("text");

        return ResponseEntity.ok(base64QRCode);
    }
}
