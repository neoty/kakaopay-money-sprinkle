package com.kakaopay.service;

import com.kakaopay.dto.Amount;
import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.User;
import com.kakaopay.entity.Receive;
import com.kakaopay.entity.Sprinkle;
import com.kakaopay.exception.business.InvalidReceiveRequestException;
import com.kakaopay.repository.ReceiveRepository;
import com.kakaopay.repository.SprinkleRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiverService {
    private final ValidationService validationService;
    private final SprinkleRedisRepository sprinkleRedisRepository;
    private final ReceiveRepository receiveRepository;
    private final SprinkleService sprinkleService;

    /**
     * 받기를 하고 받은 금액을 반환한다.
     *
     * @param user      받기 신청 유저 정보
     * @param token     뿌리기 토큰 정보
     * @return          받은 금액
     */
    public Amount receiveByToken(User user, Token token) {
        validationService.checkRoomIdOrElseThrows(user.getRoomId());
        validationService.checkUserIdOrElseThrows(user.getUserId());

        Sprinkle sprinkle = sprinkleService.getReceivableSprinkle(token, user.getRoomId());
        validationService.checkReceivableOrElseThrow(user, sprinkle);

        String cacheKey = sprinkle.getUserId() + token.getToken();
        Amount receive = new Amount(sprinkleRedisRepository.getAmountByLeftPopInList(cacheKey));
        if (receive.getAmount() == null) {
            throw new InvalidReceiveRequestException();
        }

        saveReceiveHistory(receive.getAmount(), user.getUserId(), sprinkle.getId());

        return receive;
    }

    /**
     * 받은정보를 저장한다.
     *
     * @param amount        받은 금액
     * @param userId        받은 유저 아이디
     * @param sprinkleId    뿌리기 아이디
     */
    private void saveReceiveHistory(Integer amount, String userId, UUID sprinkleId) {
        Receive receive = Receive.builder()
                .sprinkleId(sprinkleId)
                .amount(amount)
                .userId(userId)
                .build();
        receiveRepository.save(receive);
    }
}
