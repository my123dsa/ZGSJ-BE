package com.example.API_Gateway.util;

import com.example.API_Gateway.error.CustomException;
import com.example.API_Gateway.error.ErrorCode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
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
}
