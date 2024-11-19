package com.example.API_Gateway.util;


import com.example.API_Gateway.error.CustomException;
import com.example.API_Gateway.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTUtil {
    //    @Value("${org.zerock.jwt.secret}")
    private final Key key;

    public JWTUtil() {
        String settingKey="dGhpc19pc19hX3ZlcnlfbG9uZ19hbmRfc2VjdXJlX2tleV9mb3JfaHMyNTZfYWxnb3JpdGhtX2F0X2xlYXN0XzMyX2J5dGVz";
        key = Keys.hmacShaKeyFor(settingKey.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, Object> validateToken(String token,String path) throws JwtException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
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
            log.error("ExpiredJwtException----------------------");
            if (path.contains("/president/refresh"))
                return expiredJwtException.getClaims();
            throw new CustomException(ErrorCode.BAD_GATEWAY_TEST);
//            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
    }
}