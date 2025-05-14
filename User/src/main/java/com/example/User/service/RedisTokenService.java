package com.example.User.service;

import com.example.User.error.CustomException;
import com.example.User.error.ErrorCode;
import com.example.User.util.JWEUtil;
import com.example.User.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisTokenService {
    private final StringRedisTemplate redisTemplate;
    private final JWTUtil jwtUtil;
    private final JWEUtil jweUtil;
    private final DefaultRedisScript<Long> redisScript;

    @Transactional
    public void removeRefreshToken(Integer id) {
        if (redisTemplate.hasKey(id.toString()))
            redisTemplate.delete(id.toString());
    }

    @Transactional
    public void setValues(Integer id, String value, Duration duration) {
        boolean success = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(id.toString(), value, duration));
        if (!success) {
            log.warn("Refresh token not set: key already exists for id={}", id);
        }
    }

    @Transactional
    public String checkRefreshToken(Integer accessTokenId) {

        String refreshToken = redisTemplate.opsForValue().get(accessTokenId.toString());
        if (refreshToken == null)
            throw new CustomException(ErrorCode.EMPTY_REFRESH_TOKEN);

        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
        String encrypt = (String) claims.get("payload");
        Integer exp = (Integer) claims.get("exp");
        log.info("encrypt :{} exp :{}", encrypt, exp);

        Integer id = jweUtil.getIdFromDecryptJWE(encrypt);

        if (!Objects.equals(id, accessTokenId))
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        checkAndRenewRefreshToken(id, exp);
        return jwtUtil.generateToken(id, 1);
    }

    @Transactional
    public void checkAndRenewRefreshToken(Integer id, Integer exp) {
        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        Date current = new Date(System.currentTimeMillis());
        long gapTime = expTime.getTime() - current.getTime();

        if (gapTime < (1000 * 60 * 60)) {
            log.info("new Refresh Token required...");
            String oldToken = redisTemplate.opsForValue().get(id.toString());
            String newToken = jwtUtil.generateToken(id, 30);


            Long result = redisTemplate.execute(
                    redisScript,
                    Collections.singletonList(id.toString()),
                    oldToken,
                    newToken,
                    String.valueOf(Duration.ofDays(100).getSeconds())
            );

            if (result == 1L) {
                log.info("Refresh token 갱신 성공");
            } else {
                log.warn("갱신 충돌 발생 → 이전 값이 바뀜 (다른 요청으로)");
            }
        }
    }
}
