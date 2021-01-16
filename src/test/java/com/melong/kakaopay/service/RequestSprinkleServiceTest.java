package com.melong.kakaopay.service;

import com.melong.kakaopay.dto.RequestSprinkle;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SpringBootTest
class RequestSprinkleServiceTest {

    @Autowired
    private DistributionService sprinkleService;

    @Test
    void getDistributedAmount_test() {
        RequestSprinkle requestSprinkle = new RequestSprinkle();
        requestSprinkle.setAmount(100);
        requestSprinkle.setNumber(100);

        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

        int number = 13;
        int remainAmount = 13;
        List<Integer> array1 = new ArrayList<>();
        for (int i = number; i > 0; i--) {
            int maxAmount = remainAmount - (i - 1);
            System.out.println("current index => " + i);
            System.out.println("current maxAmount => " + maxAmount);
            System.out.println("current remainAmount => " + remainAmount);

            int pickAmount = 1;

            if (i==1) {
                pickAmount = maxAmount;
            } else if (maxAmount > i) {
                pickAmount = randomDataGenerator.nextInt(1, (maxAmount * 50) / 100);
            }

            remainAmount = remainAmount - pickAmount;
            array1.add(pickAmount);

            System.out.println("current pickAmount =>" + pickAmount);
            System.out.println("=======================loop end=======================");
        }

        Collections.shuffle(array1);
        System.out.println(array1);
        int total = 0;
        for (int a : array1) {
            total = total + a;
        }
        System.out.println("총사이즈:" + array1.size());
        System.out.println("총 금액더하기:" + total);








//        List<Integer> array1 = new ArrayList<>();
//        for (int i = number-1; i >= 0; i--) {
//            int maxAmount = remainAmount - i;
//            System.out.println("current index => " + i);
//            System.out.println("current maxAmount => " + maxAmount);
//            System.out.println("current remainAmount => " + remainAmount);
//
//            int pickAmount = 1;
//
//            if (i==0) {
//                pickAmount = maxAmount;
//            } else if (maxAmount > i) {
//                pickAmount = randomDataGenerator.nextInt(1, (maxAmount * 50) / 100);
//            }
//
//            remainAmount = remainAmount - pickAmount;
//            array1.add(pickAmount);
//
//            System.out.println("current pickAmount =>" + pickAmount);
//            System.out.println("=======================loop end=======================");
//        }
//
//        Collections.shuffle(array1);
//        System.out.println(array1);
//        int total = 0;
//        for (int a : array1) {
//            total = total + a;
//        }
//        System.out.println("총사이즈:" + array1.size());
//        System.out.println("총 금액더하기:" + total);


//
//        List<Integer> array = new ArrayList<>();
//        for (int i=1; i<number + 1; i++) {
//            int maxAmount =  remainAmount - (number - i);
//
//            if (maxAmount > 1) {
//                int pickAmount = randomDataGenerator.nextInt(1, maxAmount);
//                remainAmount = remainAmount - pickAmount;
//                array.add(pickAmount);
//            } else {
//                //array.add(1);
//            }
//        }


    }
}