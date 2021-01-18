package com.kakaopay.repository;

import com.kakaopay.ApplicationTest;
import com.kakaopay.entity.Receive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReceiveRepositoryTest extends ApplicationTest {
    @Autowired
    private ReceiveRepository receiveRepository;

    @BeforeEach
    void setup() {
        receiveRepository.deleteAllInBatch();
    }

    @Test
    void existsReceiveBySprinkleIdAndUserId_정보가없을떄() {
        // when
        Boolean result = receiveRepository.existsReceiveBySprinkleIdAndUserId(UUID.randomUUID(), "001");

        // assert
        assertThat(result).isFalse();
    }

    @Test
    void existsReceiveBySprinkleIdAndUserId_정보가있을때() {
        // given
        UUID sprinkleId = UUID.randomUUID();
        Receive receive = receiveRepository.save(Receive.builder().amount(1000).sprinkleId(sprinkleId).userId("001").build());

        // when
        Boolean result = receiveRepository.existsReceiveBySprinkleIdAndUserId(sprinkleId, "001");

        // assert
        assertThat(result).isTrue();
    }

    @Test
    void findAllBySprinkleId_리스트가_있을때() {
        // given
        UUID sprinkleId = UUID.randomUUID();
        List<Receive> receives = new ArrayList<>();
        receives.add(Receive.builder().amount(1000).sprinkleId(sprinkleId).userId("1").build());
        receives.add(Receive.builder().amount(1000).sprinkleId(sprinkleId).userId("002").build());
        receives.add(Receive.builder().amount(1000).sprinkleId(sprinkleId).userId("2").build());
        receives.add(Receive.builder().amount(1000).sprinkleId(sprinkleId).userId("004").build());
        receives.add(Receive.builder().amount(1000).sprinkleId(sprinkleId).userId("05").build());
        receiveRepository.saveAll(receives);

        // when
        List<Receive> receiveList = receiveRepository.findAllBySprinkleId(sprinkleId);

        // assert
        assertThat(receiveList).hasSize(receiveList.size());
    }

    @Test
    void findAllBySprinkleId_리스트가_없을때() {
        // when
        List<Receive> receiveList = receiveRepository.findAllBySprinkleId(UUID.randomUUID());

        // assert
        assertThat(receiveList).hasSize(0);
    }
}