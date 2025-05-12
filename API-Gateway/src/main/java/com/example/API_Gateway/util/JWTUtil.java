package com.example.API_Gateway.util;


import com.example.API_Gateway.error.CustomException;
import com.example.API_Gateway.error.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Map;

@Component
@Slf4j
public class JWTUtil {
    private final SecretKey key;

    public JWTUtil(@Qualifier("jwsSecretKey") SecretKey key) {
        this.key = key;
    }

    public Map<String, Object> validateToken(String token, String path) throws JwtException {
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