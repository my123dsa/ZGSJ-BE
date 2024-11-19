package com.example.Attendance.util;



import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class Cryptography {

    private static final String ALGORITHM = "AES";
    private static final String FIXED_KEY = "myFixedSecretKey";
    private final SecretKey secretKey;

    public Cryptography() {
        byte[] keyBytes = FIXED_KEY.getBytes(); // 문자열을 바이트 배열로 변환
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String decrypt(String encryptedEmail) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
            byte[] decodedBytes = Base64.getUrlDecoder().decode(encryptedEmail);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_DECRYPTION);
        }
    }
}