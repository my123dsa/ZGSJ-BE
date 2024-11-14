package com.example.User.util;


import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTUtil {
    //    @Value("${org.zerock.jwt.secret}")
    private String key =
            "dGhpc19pc19hX3ZlcnlfbG9uZ19hbmRfc2VjdXJlX2tleV9mb3JfaHMyNTZfYWxnb3JpdGhtX2F0X2xlYXN0XzMyX2J5dGVz";
    private static final String ALGORITHM = "AES";
    private static final String FIXED_KEY = "myFixedSecretKey";
    private final SecretKey secretKey;


    public JWTUtil() {
        byte[] keyBytes = FIXED_KEY.getBytes(); // 문자열을 바이트 배열로 변환
        this.secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String generateToken(Integer id, int days) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> encrypted = encrypt(id);

        int time = 60 * 24 * days; //테스트는 분단위로 나중에 60*24 (일)단위변경

        ZonedDateTime nowUtc = ZonedDateTime.now(ZoneId.of("UTC"));

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(encrypted)
                .setIssuedAt(Date.from(nowUtc.toInstant()))
                .setExpiration(Date.from(nowUtc.plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();
        return jwtStr;
    }

    public Map<String, Object> validateToken(String token) throws JwtException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException----------------------");
            throw new CustomException(ErrorCode.MALFORM_TOKEN);
        } catch (SecurityException signatureException) {
            log.error("SignatureException----------------------");
            throw new CustomException(ErrorCode.BADSIGN_TOKEN);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
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
}