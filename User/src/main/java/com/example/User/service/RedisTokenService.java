package com.example.User.service;

import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class RedisTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JWTUtil jwtUtil;
    private final ValueOperations<String, String> valueOps;

    public RedisTokenService(RedisTemplate<String, String> redisTemplate, JWTUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.jwtUtil = jwtUtil;
    }

    public void removeRefreshToken(Integer id) {
        if (redisTemplate.hasKey(id.toString()))
            redisTemplate.delete(id.toString());
    }

    public void setValues(Integer id, String value, Duration duration) {
        valueOps.set(id.toString(), value, duration);
    }

    public String getValue(String key) {
        if (valueOps.get(key) == null)
            return "none";
        return String.valueOf(valueOps.get(key));
    }

    public String checkRefreshToken(Integer accessTokenId) {

        String refreshToken =valueOps.get(accessTokenId.toString());
        if (refreshToken == null)
            throw new CustomException(ErrorCode.EMPTY_REFRESH_TOKEN);

        Map<String, Object> claims= jwtUtil.validateToken(refreshToken);
        String encrypt= (String)claims.get("payload");
        Integer exp = (Integer) claims.get("exp");
        log.info("encrypt"+encrypt +"exp"+exp);

        Integer id = jwtUtil.decrypt(encrypt);

        if(!Objects.equals(id, accessTokenId))
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        checkAndRenewRefreshToken(id,exp);
        return jwtUtil.generateToken(id, 1);
    }
    public void checkAndRenewRefreshToken(Integer id,Integer exp){
        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        Date current = new Date(System.currentTimeMillis());
        long gapTime = (expTime.getTime() - current.getTime());

        //RefrshToken이 3일도 안남았다면..
        if (gapTime < (1000 * 60 * 60)) {
            //if(gapTime < (1000 * 60 * 60 * 24 * 3  ) ){
            log.info("new Refresh Token required...  ");
            String userRefreshToken = jwtUtil.generateToken(id, 30);
            setValues(id, userRefreshToken, Duration.ofDays(100));
        }
    }
}
