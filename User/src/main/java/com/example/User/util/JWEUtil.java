package com.example.User.util;


import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.util.Map;

@Component
@Slf4j
public class JWEUtil {
    private final SecretKey key;

    public JWEUtil(@Qualifier("jweSecretKey") SecretKey key) {
        this.key = key;
    }

    public Integer getIdFromDecryptJWE(String jwt) {
        Map<String, Object> claims = decryptWithNimbus(jwt);
        Object payload = claims.get("payload");

        if (!(payload instanceof Number)) {
            throw new CustomException(ErrorCode.INVALID_DECRYPTION);
        }
        int id = ((Number) payload).intValue();
        return id;
    }

    private Map<String, Object> decryptWithNimbus(String jweToken) {
        try {
            JWEObject jweObject = JWEObject.parse(jweToken);
            jweObject.decrypt(new DirectDecrypter(key));
            return jweObject.getPayload().toJSONObject();
        } catch (ParseException | JOSEException e) {
            log.error("JWE 복호화 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.INVALID_DECRYPTION);
        }
    }

    public String encryptPayload(Integer id) {
        Map<String, Object> claimsMap = Map.of("payload", id, "role", "user");
        JWEObject jwe = buildJweObject(claimsMap);

        try {
            jwe.encrypt(new DirectEncrypter(key));
        } catch (JOSEException e) {
            throw new CustomException(ErrorCode.INVALID_ENCRYPTION);
        }
        return jwe.serialize();
    }

    private JWEObject buildJweObject(Map<String, Object> claims) {
        JWEHeader header = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                .contentType("JWT") // Optional
                .build();

        return new JWEObject(header, new Payload(claims));
    }
}
