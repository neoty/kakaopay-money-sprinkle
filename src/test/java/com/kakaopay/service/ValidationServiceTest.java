package com.kakaopay.service;

import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.User;
import com.kakaopay.exception.business.*;
import com.kakaopay.repository.SprinkleRepository;
import com.kakaopay.entity.Receive;
import com.kakaopay.entity.Sprinkle;
import com.kakaopay.repository.ReceiveRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ValidationServiceTest {
    @Autowired
    private ValidationService validationService;

    @Autowired
    private ReceiveRepository receiveRepository;

    @Autowired
    private SprinkleRepository sprinkleRepository;

    @BeforeEach
    void setup() {
        receiveRepository.deleteAllInBatch();
        sprinkleRepository.deleteAllInBatch();
    }

    @Test
    void checkUserIdOrElseThrows() {
        validationService.checkUserIdOrElseThrows("1");
        validationService.checkUserIdOrElseThrows("01");
        validationService.checkUserIdOrElseThrows("014");

        assertThatThrownBy(() -> {
            validationService.checkUserIdOrElseThrows("01e");
        }).isInstanceOf(InvalidUserIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkUserIdOrElseThrows("");
        }).isInstanceOf(InvalidUserIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkUserIdOrElseThrows("$x");
        }).isInstanceOf(InvalidUserIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkUserIdOrElseThrows("asdf");
        }).isInstanceOf(InvalidUserIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkUserIdOrElseThrows("qwe");
        }).isInstanceOf(InvalidUserIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkUserIdOrElseThrows(RandomStringUtils.randomAlphanumeric(300));
        }).isInstanceOf(InvalidUserIdException.class);
    }

    @Test
    void checkRoomIdOrElseThrows() {
        validationService.checkRoomIdOrElseThrows(RandomStringUtils.randomAlphabetic(10));

        assertThatThrownBy(() -> {
            validationService.checkRoomIdOrElseThrows(RandomStringUtils.randomAlphabetic(9));
        }).isInstanceOf(InvalidRoomIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkRoomIdOrElseThrows(RandomStringUtils.randomAlphabetic(11));
        }).isInstanceOf(InvalidRoomIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkRoomIdOrElseThrows("");
        }).isInstanceOf(InvalidRoomIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkRoomIdOrElseThrows(RandomStringUtils.randomNumeric(10));
        }).isInstanceOf(InvalidRoomIdException.class);

        assertThatThrownBy(() -> {
            validationService.checkRoomIdOrElseThrows(RandomStringUtils.randomAlphanumeric(9) + "1");
        }).isInstanceOf(InvalidRoomIdException.class);
    }



    @Test
    void checkDistributeAmountAndNumberOrElseThrow() {
        validationService.checkDistributeAmountAndNumberOrElseThrow(1, 1);
        validationService.checkDistributeAmountAndNumberOrElseThrow(10, 5);

        assertThatThrownBy(() -> {
            validationService.checkDistributeAmountAndNumberOrElseThrow(0, 0);
        }).isInstanceOf(InvalidSprinkleRequestException.class);


        assertThatThrownBy(() -> {
            validationService.checkDistributeAmountAndNumberOrElseThrow(1, 0);
        }).isInstanceOf(InvalidSprinkleRequestException.class);


        assertThatThrownBy(() -> {
            validationService.checkDistributeAmountAndNumberOrElseThrow(0, 1);
        }).isInstanceOf(InvalidSprinkleRequestException.class);

        assertThatThrownBy(() -> {
            validationService.checkDistributeAmountAndNumberOrElseThrow(1, 10);
        }).isInstanceOf(InvalidSprinkleRequestException.class);
    }

    @Test
    void checkReceivableOrElseThrow() {
        // 시간체크
        assertThatThrownBy(() -> {
            validationService.checkReceivableOrElseThrow(new User("dummy"), Sprinkle.builder().createdAt(LocalDateTime.now().minusMinutes(11)).build());
        }).isInstanceOf(InvalidReceiveRequestException.class);

        // 같은 뿌리기 유저 아이에 같인 받기 유저 아이디
        assertThatThrownBy(() -> {
            Sprinkle sprinkle = Sprinkle.builder().userId("000").createdAt(LocalDateTime.now()).build();
            validationService.checkReceivableOrElseThrow(new User("000"), sprinkle);
        }).isInstanceOf(InvalidReceiveRequestException.class);

        // 뿌리기 한 대화방과 받기신청한 대화방이 다름
        assertThatThrownBy(() -> {
            Sprinkle sprinkle = Sprinkle.builder().userId("111").roomId("zzzzzzzzzz").createdAt(LocalDateTime.now()).build();
            User user = new User("222");
            user.setRoomId("cccccccccc");
            validationService.checkReceivableOrElseThrow(user, sprinkle);
        }).isInstanceOf(InvalidReceiveRequestException.class);

        // 중복 신청 확인
        assertThatThrownBy(() -> {
            // given
            final String receiveUserId = "222";
            final UUID sprinkleId = UUID.randomUUID();
            receiveRepository.save(Receive.builder().amount(1000).sprinkleId(sprinkleId).userId(receiveUserId).build());

            Sprinkle sprinkle = Sprinkle.builder().id(sprinkleId).userId("111").roomId("zzzzzzzzzz").createdAt(LocalDateTime.now()).build();
            User user = new User(receiveUserId);
            user.setRoomId("zzzzzzzzzz");
            validationService.checkReceivableOrElseThrow(user, sprinkle);

        }).isInstanceOf(DuplicateReceiveRequestException.class);
    }

    @Test
    void checkTokenDuplicateOrThrow() {
        validationService.checkTokenDuplicateOrThrow(new Token("aaa"), "1");

        assertThatThrownBy(() -> {
            sprinkleRepository.save(Sprinkle.builder().userId("1").token("aaa").roomId(RandomStringUtils.randomAlphabetic(10)).amount(1000).build());
            validationService.checkTokenDuplicateOrThrow(new Token("aaa"), "1");
        }).isInstanceOf(TokenDuplicateRetryException.class);
    }
}