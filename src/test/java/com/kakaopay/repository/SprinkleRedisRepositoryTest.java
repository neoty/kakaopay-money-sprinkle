package com.kakaopay.repository;

import com.kakaopay.constant.Constant;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SprinkleRedisRepositoryTest {
    @Autowired
    private SprinkleRedisRepository sprinkleRedisRepository;

    @Test
    void saveAmountAndSetExpireSecond_금액_리스트_저장() {
        List<Integer> integers = new ArrayList<>();
        for (int i=0; i<1000; i++) {
            integers.add((int) (Math.random() * 1000));
        }
        sprinkleRedisRepository.saveAmountAndSetExpireSecond(RandomStringUtils.randomAlphabetic(10), integers, Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND);
    }

    @Test
    void getAmountByLeftPopInList_금액_리스트에서_하나가져오기() {
        List<Integer> integers = new ArrayList<>();
        for (int i=0; i<10; i++) {
            integers.add((int) (Math.random() * 1000));
        }

        String key = RandomStringUtils.randomAlphabetic(10);
        sprinkleRedisRepository.saveAmountAndSetExpireSecond(key, integers, Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND);

        Integer amount = sprinkleRedisRepository.getAmountByLeftPopInList(key);
        assertThat(amount).isGreaterThan(0);
    }

    @Test
    void getAmountByLeftPopInList_더이상남은금액이_없을때_널반환() {
        List<Integer> integers = new ArrayList<>();
        for (int i=0; i<1; i++) {
            integers.add((int) (Math.random() * 1000));
        }

        String key = RandomStringUtils.randomAlphabetic(10);
        sprinkleRedisRepository.saveAmountAndSetExpireSecond(key, integers, Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND);

        Integer existsAmount = sprinkleRedisRepository.getAmountByLeftPopInList(key);
        assertThat(existsAmount).isGreaterThan(0);

        Integer nullAmount = sprinkleRedisRepository.getAmountByLeftPopInList(key);
        assertThat(nullAmount).isNull();
    }
}