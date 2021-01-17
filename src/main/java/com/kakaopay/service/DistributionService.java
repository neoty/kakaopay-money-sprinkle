package com.kakaopay.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DistributionService {
    private final ValidationService validationService;

    /**
     * 분배금액을 분배 숫자만큼 분배하여 섞는다
     *
     * @param amount                분배금액
     * @param distributionNumber    분개 숫자
     * @return                      총 분배 금액 리스트
     */
    List<Integer> getShuffledAmount(int amount, int distributionNumber) {

        validationService.checkDistributeAmountAndNumberOrElseThrow(amount, distributionNumber);

        List<Integer> distributedAmount = distributeAmountByNumber(amount, distributionNumber);
        Collections.shuffle(distributedAmount);

        return distributedAmount;
    }

    /**
     * 대상 금액을 대상 숫자에 맞게 분배한다
     *
     * @param amount                대상 금액
     * @param distributionNumber    대상 숫자
     * @return                      총 분배 금액 리스트
     */
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
                distributionAmount = randomDataGenerator.nextInt(1, (int) Math.round(distributionMaxAmount * 0.5));
            }

            remainAmount = remainAmount - distributionAmount;
            distributedAmount.add(distributionAmount);
        }

        return distributedAmount;
    }
}
