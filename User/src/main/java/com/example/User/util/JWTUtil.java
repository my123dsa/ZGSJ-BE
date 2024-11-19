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

public class JWTUtil {
    //    @Value("${org.zerock.jwt.secret}")
    private final String key;
//            = "dGhpc19pc19hX3ZlcnlfbG9uZ19hbmRfc2VjdXJlX2tleV9mb3JfaHMyNTZfYWxnb3JpdGhtX2F0X2xlYXN0XzMyX2J5dGVz";
    private final CryptoUtil cryptoUtil;

    public JWTUtil(CryptoUtil cryptoUtil) {
        this.cryptoUtil = cryptoUtil;
        this.key ="dGhpc19pc19hX3ZlcnlfbG9uZ19hbmRfc2VjdXJlX2tleV9mb3JfaHMyNTZfYWxnb3JpdGhtX2F0X2xlYXN0XzMyX2J5dGVz";
    }

    public String generateToken(Integer id, int days) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> encrypted = cryptoUtil.encrypt(id);

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


}