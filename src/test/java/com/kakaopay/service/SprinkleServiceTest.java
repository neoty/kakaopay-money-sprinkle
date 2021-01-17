package com.kakaopay.service;

import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.SprinkleSubmit;
import com.kakaopay.dto.request.User;
import com.kakaopay.repository.SprinkleRepository;
import com.kakaopay.repository.ReceiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class SprinkleServiceTest {

    @Autowired
    private SprinkleService sprinkleService;

    @Autowired
    private SprinkleRepository sprinkleRepository;

    @Autowired
    private ReceiveRepository receiveRepository;

    @MockBean
    private DistributionService distributionService;


    @BeforeEach
    void setup() {
        sprinkleRepository.deleteAllInBatch();
        receiveRepository.deleteAllInBatch();
    }

    @Test
    void sprinkleAndGenerateToken_뿌리기_정상() {
        // given
        User user = new User("1");
        user.setRoomId("abcdeABCDE");

        SprinkleSubmit sprinkleSubmit = new SprinkleSubmit();
        sprinkleSubmit.setNumber(2);
        sprinkleSubmit.setAmount(1000);

        // when
        List<Integer> list = new ArrayList<>();
        list.add(500);
        list.add(500);
        when(distributionService.getShuffledAmount(sprinkleSubmit.getAmount(), sprinkleSubmit.getNumber())).thenReturn(list);

        // assert
        Token token = sprinkleService.sprinkleAndGenerateToken(user, sprinkleSubmit);
        assertThat(token.getToken()).matches("^[a-zA-Z]{3}$");
    }

}