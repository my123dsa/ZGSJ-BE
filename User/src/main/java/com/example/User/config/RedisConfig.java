package com.example.User.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisConfig {
//
//    @Value("${spring.data.redis.host}")
//    private String host;
//
//    @Value("${spring.data.redis.port}")
//    private int port;
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(host, port);
//    }
//
//    @Bean
//    @Primary
//    public RedisTemplate<String, String> redisTemplate() {
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//
//        // Redis를 연결합니다.
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//
//        // Key-Value 형태로 직렬화를 수행합니다.
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//
////        //이후 거래 내역 저장시 객체 이거 사용할 듯?
////        Jackson2JsonRedisSerializer<Object> jsonSerializer =
////                new Jackson2JsonRedisSerializer<>(Object.class);
////        redisTemplate.setValueSerializer(jsonSerializer);
////        // Hash Key-Value 형태로 직렬화를 수행합니다.
////        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
////        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
////
////        // 기본적으로 직렬화를 수행합니다.
////        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
//}