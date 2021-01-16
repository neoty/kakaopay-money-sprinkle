package com.melong.kakaopay.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class SprinkleRedisRepository {
    private static final int EXPIRE_SECOND = 600;
    private final RedisTemplate<String, Integer> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public void saveAmount(String key, List<Integer> amount) {
        //stringRedisTemplate.opsForList().leftPushAll(key, String.valueOf(amount));
        redisTemplate.opsForList().leftPushAll(key, amount);
        redisTemplate.expire(key, EXPIRE_SECOND, TimeUnit.SECONDS);
    }

    public Integer getNextAmount(String key) {
        return redisTemplate.opsForList().leftPop(key);
        //String pop = stringRedisTemplate.opsForList().leftPop(key);
        //return Integer.parseInt(pop);
    }

    public Boolean checkKeyExists(String key) {
        return redisTemplate.hasKey(key);
    }
}
