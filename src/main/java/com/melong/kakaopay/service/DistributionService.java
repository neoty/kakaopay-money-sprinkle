package com.melong.kakaopay.service;

import com.melong.kakaopay.exception.SprinkleValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DistributionService {
    List<Integer> getDistributedAmount(int amount, int distributionNumber) {

        checkSprinkleAmountAndNumber(amount, distributionNumber);

        List<Integer> distributedAmount = distributeAmountByNumber(amount, distributionNumber);
        Collections.shuffle(distributedAmount);

        return distributedAmount;
    }

    private List<Integer> distributeAmountByNumber(int amount, int distributionNumber) {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        int remainAmount = amount;
        List<Integer> distributedAmount = new ArrayList<>();

        for (int currentNumber = distributionNumber; currentNumber > 0; currentNumber--) {
            // 기본 최소 보장 금액
            int distributionAmount = 1;

            // 각 배열마다 최소 1이상 보존을 위해 현재 인덱스에서 뒤에 남은 인덱스만큼 금액을 뺀다
            int distributionMaxAmount = remainAmount - (currentNumber - 1);

            if (currentNumber == 1) {
                // 마지막 인원일 경우 남은 금액 모두
                distributionAmount = distributionMaxAmount;
            } else if (distributionMaxAmount > currentNumber) {
                // 인원별 최소 보장 금액 이상으로 금액을 배분 가능할 시
                distributionAmount = randomDataGenerator.nextInt(1, (distributionMaxAmount * 50) / 100);
            }

            remainAmount = remainAmount - distributionAmount;
            distributedAmount.add(distributionAmount);
        }

        return distributedAmount;
    }

    private void checkSprinkleAmountAndNumber(int amount, int distributionNumber) {
        if (amount < 1 || distributionNumber < 1) {
            throw new SprinkleValidationException();
        }
        log.info(String.valueOf(distributionNumber));
        if (amount < distributionNumber) {
            throw new SprinkleValidationException();
        }
    }
}
