package com.example.API_Gateway.util;

import com.example.API_Gateway.error.CustomException;
import com.example.API_Gateway.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.Base64;


@Component
@Slf4j
public class CryptoUtil {

    private final String ALGORITHM;
    private final SecretKey secretKey;

    public CryptoUtil() {
        ALGORITHM="AES";
        String FIXED_KEY = "myFixedSecretKey";
        byte[] keyBytes = FIXED_KEY.getBytes(); // 문자열을 바이트 배열로 변환
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public Integer decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            ByteBuffer buffer = ByteBuffer.wrap(decryptedBytes);
            return buffer.getInt();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_DECRYPTION);
        }
    }
}
