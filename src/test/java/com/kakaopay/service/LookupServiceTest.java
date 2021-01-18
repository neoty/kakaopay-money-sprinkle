package com.kakaopay.service;

import com.kakaopay.ApplicationTest;
import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.User;
import com.kakaopay.exception.business.NotFoundSprinkleException;
import com.kakaopay.repository.SprinkleRepository;
import com.kakaopay.dto.reponse.Lookup;
import com.kakaopay.entity.Receive;
import com.kakaopay.entity.Sprinkle;
import com.kakaopay.repository.ReceiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class LookupServiceTest extends ApplicationTest {
    @Autowired
    private LookupService lookupService;

    @Autowired
    private SprinkleRepository sprinkleRepository;

    @Autowired
    private ReceiveRepository receiveRepository;

    @BeforeEach
    void setup() {
        sprinkleRepository.deleteAllInBatch();
        receiveRepository.deleteAllInBatch();
    }

    @Test
    void getSprinkleStats_존재() {
        // given
        final Token token = new Token("AAA");
        final String sprinkleOwnerUserId = "000";
        final String receiveUserId1 = "1";
        final String receiveUserId2 = "2";
        final Integer sprinkleAmount = 1000;
        final Integer receiveAmount = 500;
        Sprinkle sprinkle = sprinkleRepository.save(Sprinkle.builder().amount(sprinkleAmount).roomId("abcdeABCDE").token("ABC").token(token.getToken()).userId(sprinkleOwnerUserId).build());
        List<Receive> receiveList = new ArrayList<>();
        receiveList.add(Receive.builder().amount(receiveAmount).sprinkleId(sprinkle.getId()).userId(receiveUserId1).build());
        receiveList.add(Receive.builder().amount(receiveAmount).sprinkleId(sprinkle.getId()).userId(receiveUserId2).build());
        List<Receive> receives = receiveRepository.saveAll(receiveList);

        // when
        Lookup lookup = lookupService.getSprinkleStats(new User(sprinkleOwnerUserId), token);

        //assert
        assertThat(lookup).isNotNull();
        assertThat(lookup.getSprinkleAmount()).isEqualTo(sprinkleAmount);
        assertThat(lookup.getSprinkleTime()).isNotNull();
        lookup.getReceiveInformation().forEach(receiveInformation -> {
            assertThat(receiveInformation.getReceivedAmount()).isEqualTo(receiveAmount);
            assertThat(receiveInformation.getUserId()).matches("[0-9]{1}");
        });
    }

    @Test
    void getSprinkleStats_존재_하지않음() {
        // given
        final Token token = new Token("aaa");
        final String sprinkleOwnerUserId = "111";

        // asset
        assertThatThrownBy(() -> {
            lookupService.getSprinkleStats(new User(sprinkleOwnerUserId), token);
        }).isInstanceOf(NotFoundSprinkleException.class);
    }
}