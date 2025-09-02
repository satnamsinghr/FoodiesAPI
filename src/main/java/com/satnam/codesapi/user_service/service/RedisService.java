package com.satnam.codesapi.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Set value without TTL
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Set value with TTL
    public void setValue(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    // Get value (will already be deserialized into Map or your object)
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Delete key
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}
