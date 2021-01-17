package com.kakaopay.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DistributionServiceTest {
    @Autowired
    private DistributionService distributionService;

    @Test
    void getShuffledAmount_1_1_테스트() {
        int amount = 1;
        int number = 1;
        // when
        List<Integer> list = distributionService.getShuffledAmount(amount, number);

        // test
        assertThat(list).hasSize(number);
        assertThat(list.get(0)).isEqualTo(1);
    }

    @Test
    void getShuffledAmount_10_10_테스트() {
        int amount = 10;
        int number = 10;
        // when
        List<Integer> list = distributionService.getShuffledAmount(amount, number);

        // test
        assertThat(list).hasSize(number);
        for (int i=0; i<list.size(); i++) {
            assertThat(list.get(i)).isEqualTo(1);
        }
    }

    @Test
    void getShuffledAmount_총_합계금액_테스트() {
        int amount = 15531302;
        int number = 150;
        // when
        List<Integer> list = distributionService.getShuffledAmount(amount, number);

        // test
        assertThat(list).hasSize(number);

        // test
        int totalAmount = list.stream().mapToInt(integer -> integer).sum();
        assertThat(totalAmount).isEqualTo(amount);
    }
}