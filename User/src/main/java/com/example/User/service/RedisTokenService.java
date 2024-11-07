package com.example.User.service;

import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JWTUtil jwtUtil;

    public void setValues(String key, String value, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    public String getValue(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null)
            return "none";
        return String.valueOf(values.get(key));
    }

    public String checkRefreshToken(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        String refreshToken =(String)values.get(key);
        if (refreshToken == null)
            throw new CustomException(ErrorCode.EMPTY_REFRESH_TOKEN);

        Map<String, Object> claims= jwtUtil.validateToken(refreshToken);
        String encrypt= (String)claims.get("payload");
        Integer exp = (Integer) claims.get("exp");
        log.info("encrypt"+encrypt +"exp"+exp);

        String email = jwtUtil.decrypt(encrypt);

        if(!email.equals(key))
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        checkAndRenewRefreshToken(email,exp);
        return jwtUtil.generateToken(email, 1);
    }
    public void checkAndRenewRefreshToken(String email,Integer exp){
        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        Date current = new Date(System.currentTimeMillis());
        long gapTime = (expTime.getTime() - current.getTime());

        //RefrshToken이 3일도 안남았다면..
        if (gapTime < (1000 * 60 * 60)) {
            //if(gapTime < (1000 * 60 * 60 * 24 * 3  ) ){
            log.info("new Refresh Token required...  ");
            String userRefreshToken = jwtUtil.generateToken(email, 30);
            setValues(email, userRefreshToken, Duration.ofDays(100));
        }
    }
}
