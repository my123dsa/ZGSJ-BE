package com.example.User.util;


import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Component
@Slf4j

public class CryptoUtil {

    private final SecureRandom rand;

    private final int PASSWORD_LENGTH;
    private final String ALGORITHM;
    private final SecretKey secretKey;

    public CryptoUtil() {
        ALGORITHM ="AES";
        String FIXED_KEY = "myFixedSecretKey";
        this.rand = new SecureRandom();
        PASSWORD_LENGTH = 10;
        byte[] keyBytes = FIXED_KEY.getBytes(); // 문자열을 바이트 배열로 변환
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    // 임의의 비밀번호 생성
    public String makeRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for(int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomNum = rand.nextInt(62); // 총 62개 문자 (10 + 26 + 26)

            if (randomNum < 10) {
                password.append((char)(randomNum + 48));  // 0-9
            } else if (randomNum < 36) {
                password.append((char)(randomNum - 10 + 65));  // A-Z
            } else {
                password.append((char)(randomNum - 36 + 97));  // a-z
            }
        }
        return password.toString();
    }

    public String makeRandomInteger() {
        return String.format("%06d", rand.nextInt(1000000));
    }

    public Map<String, Object> encrypt(Integer id) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(4); // Integer는 4바이트
            buffer.putInt(id);
            byte[] idBytes = buffer.array();

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
            byte[] encryptedBytes = cipher.doFinal(idBytes);
            String s = Base64.getEncoder().encodeToString(encryptedBytes);
            return Map.of("payload", s);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_ENCRYPTION);
        }
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

    public String encryptByEmail(String email) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
            byte[] encryptedBytes = cipher.doFinal(email.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_ENCRYPTION);
        }
    }
}
