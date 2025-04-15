package com.example.User.util;


import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.util.Map;

@Component
//@RequiredArgsConstructor
@Slf4j
public class JWEUtil {
    private final SecretKey key;

    public JWEUtil() {
//        String settingKey="dGhpc19pc19hX3ZlcnlfbG9uZ19hbmRfc2VjdXJl";
//        key = Keys.hmacShaKeyFor(settingKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = "your-predefined-secret-key".getBytes(StandardCharsets.UTF_8);
        key = generateKeyFromPassword("doif023nsdfoasdjfkosadfksajd",keyBytes);
    }

    public static SecretKey generateKeyFromPassword(String password, byte[] salt) {
        try {
            // 비밀번호와 salt로 키를 생성하는 방법
            // PBEKeySpec을 이용해 password와 salt로 key를 생성
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);

            // SecretKeyFactory를 사용하여 키 생성
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();  // 비밀 키 생성

            // 생성된 바이트 배열로 SecretKey 생성
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("키 생성 중 오류 발생", e);
        }
    }

    public Integer getIdFromDecrpytJWE(String jwt) {
        try {
            Map<String, Object> claims = decryptWithNimbus(jwt);
            Long idLong = (Long) claims.get("payload");
            Integer id =idLong.intValue();
            return id;
        }catch (Exception e) {
            log.error(e.getMessage());
            throw  new CustomException(ErrorCode.INVALID_DECRYPTION);
        }
    }

    private Map<String, Object> decryptWithNimbus(String jweToken) {
        try {
            // 1. JWE 토큰 파싱
            JWEObject jweObject = JWEObject.parse(jweToken);

            jweObject.decrypt(new DirectDecrypter(key));
            Payload payload = jweObject.getPayload();
            // 3. 페이로드 추출 및 Map 변환
            Map<String, Object> claims =  payload.toJSONObject();
            return claims;
        } catch (ParseException e) {
            log.error("JWE 파싱 실패: {}", e.getMessage());
//            throw new IllegalArgumentException("잘못된 JWE 형식입니다.", e);
            throw  new CustomException(ErrorCode.INVALID_DECRYPTION);
        } catch (JOSEException e) {
            log.error("JWE 복호화 실패: {}", e.getMessage());
            throw  new CustomException(ErrorCode.INVALID_DECRYPTION);

        } catch (Exception e) {
            log.error("JWE 처리 중 알 수 없는 오류: {}", e.getMessage());
            throw  new CustomException(ErrorCode.INVALID_DECRYPTION);
        }
    }

    public String encryptJWT(Integer id) {
        // 1. JWT payload 생성
        Map<String, Object> claimsMap = Map.of(
                "payload",id,
                "role","user");
        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                .contentType("JWT") // 선택사항
                .build();
        // 2. JWT -> JWE 객체 생성
        JWEObject jwe =new JWEObject(
                header,
                new Payload(claimsMap)
        );

        try {
            jwe.encrypt(new DirectEncrypter( key));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return jwe.serialize();
    }
}
