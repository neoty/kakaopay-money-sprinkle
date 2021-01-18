package com.kakaopay.service;

import com.kakaopay.ApplicationTest;
import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.User;
import com.kakaopay.exception.business.InvalidReceiveRequestException;
import com.kakaopay.dto.Amount;
import com.kakaopay.entity.Sprinkle;
import com.kakaopay.repository.SprinkleRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReceiverServiceTest extends ApplicationTest {

    @Autowired
    private ReceiverService receiverService;

    @MockBean
    private SprinkleService sprinkleService;

    @MockBean
    SprinkleRedisRepository sprinkleRedisRepository;

    @Test
    void receiveByToken_정상받기() {
        // given
        final Integer receiveAmount = 500;
        Token token = new Token("aaa");
        User user = new User("2");
        user.setRoomId("abcdeABCDE");

        Sprinkle sprinkle = Sprinkle.builder().
                token(token.getToken())
                .roomId(user.getRoomId()).userId("1").amount(1000)
                .id(UUID.randomUUID()).createdAt(LocalDateTime.now())
                .build();

        when(sprinkleService.getReceivableSprinkle(token, user.getRoomId())).thenReturn(sprinkle);
        when(sprinkleRedisRepository.getAmountByLeftPopInList(sprinkle.getUserId() + token.getToken())).thenReturn(receiveAmount);

        // when
        Amount amount = receiverService.receiveByToken(user, token);

        // assert
        assertThat(amount.getAmount()).isEqualTo(receiveAmount);
    }


    @Test
    void receiveByToken_받을금액이없을때() {
        // given
        final Integer receiveAmount = 500;
        Token token = new Token("aaa");
        User user = new User("2");
        user.setRoomId("abcdeABCDE");

        Sprinkle sprinkle = Sprinkle.builder().
                token(token.getToken())
                .roomId(user.getRoomId()).userId("1").amount(1000)
                .id(UUID.randomUUID()).createdAt(LocalDateTime.now())
                .build();

        // when
        when(sprinkleService.getReceivableSprinkle(token, user.getRoomId())).thenReturn(sprinkle);
        when(sprinkleRedisRepository.getAmountByLeftPopInList(sprinkle.getUserId() + token.getToken())).thenReturn(null);

        // assert
        assertThatThrownBy(() -> {
            receiverService.receiveByToken(user, token);
        }).isInstanceOf(InvalidReceiveRequestException.class);
    }

}