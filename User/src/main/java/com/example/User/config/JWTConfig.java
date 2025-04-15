package com.example.User.config;

import com.example.User.util.CryptoUtil;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JWTConfig {
    @Value("${jwt.jwe.key}")
    private String jweKeyString;

    @Value("${jwt.jws.key}")
    private String jwsKeyString;

    @Bean
    public SecretKey jweSecretKey() {
        byte[] salt = "your-predefined-secret-key".getBytes(StandardCharsets.UTF_8);
        return CryptoUtil.generateKeyFromPassword(jweKeyString, salt);
    }

    @Bean
    public SecretKey jwsSecretKey() {
        return Keys.hmacShaKeyFor(jwsKeyString.getBytes(StandardCharsets.UTF_8));
    }
}
