package com.kakaopay.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class SprinkleRedisRepository {
    private final RedisTemplate<String, Integer> redisTemplate;

    /**
     * 금액 리스트를 저장하고 만료 초를 설정 한다.
     *
     * @param key               cache key
     * @param amount            금액 리스트
     * @param expireSecond      만료 시간
     */
    public void saveAmountAndSetExpireSecond(String key, List<Integer> amount, Integer expireSecond) {
        redisTemplate.opsForList().leftPushAll(key, amount);
        redisTemplate.expire(key, expireSecond, TimeUnit.SECONDS);
    }

    /**
     * Integer list 에서 left pop 하여 반환한다
     *
     * @param key       cache key
     * @return          left pop 정보
     */
    public Integer getAmountByLeftPopInList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }
}
