package com.kakaopay.service;

import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.SprinkleSubmit;
import com.kakaopay.dto.request.User;
import com.kakaopay.exception.business.NotFoundSprinkleException;
import com.kakaopay.repository.SprinkleRepository;
import com.kakaopay.constant.Constant;
import com.kakaopay.entity.Sprinkle;
import com.kakaopay.repository.SprinkleRedisRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SprinkleService {
    private final ValidationService validationService;
    private final DistributionService distributionService;
    private final SprinkleRedisRepository sprinkleRedisRepository;
    private final SprinkleRepository sprinkleRepository;

    /**
     * 뿌리기 정보를 생성하고 토큰을 반환한다
     *
     * @param user              뿌리기 신청자 정보
     * @param sprinkleSubmit    뿌리기 요청 정보
     * @return                  뿌리기 토큰
     */
    @Transactional
    public Token sprinkleAndGenerateToken(User user, SprinkleSubmit sprinkleSubmit) {
        validationService.checkRoomIdOrElseThrows(user.getRoomId());
        validationService.checkUserIdOrElseThrows(user.getUserId());

        Token token = new Token(generateToken());
        validationService.checkTokenDuplicateOrThrow(token, user.getUserId());

        List<Integer> distributedAmount = distributionService.getShuffledAmount(
                sprinkleSubmit.getAmount(), sprinkleSubmit.getNumber());

        saveSprinkleHistory(sprinkleSubmit.getAmount(), token, user);

        String cacheKey = user.getUserId() + token.getToken();
        saveSprinkleCache(cacheKey, distributedAmount);


        return token;
    }

    /**
     * 받기 가능한 뿌리기 정보 조회
     *
     * @param token         뿌리기 토큰
     * @param roomId        사용자 요청 대화창 아이디
     * @return Sprinkle     뿌리기 정보
     */
    Sprinkle getReceivableSprinkle(Token token, String roomId) {
        return sprinkleRepository.findTop1ByTokenAndRoomIdAndCreatedAtAfter(
                token.getToken(),
                roomId,
                LocalDateTime.now().minusSeconds(Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND))
                .orElseThrow(NotFoundSprinkleException::new);
    }

    /**
     * 받기 가능한 금액리스트를 list로 REDIS에 저장한다.
     *
     * @param key       cache key
     * @param amount    받기 금액 리스트
     */
    private void saveSprinkleCache(String key, List<Integer> amount) {
        sprinkleRedisRepository.saveAmountAndSetExpireSecond(key, amount, Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND);
    }

    /**
     * 뿌리기 신청 정보를 데이터베이스에 저장한다.
     *
     * @param amount    뿌리기 신청 금액
     * @param token     뿌리기 토큰 정보
     * @param user      뿌리기 신청한 유저 정보
     */
    private void saveSprinkleHistory(Integer amount, Token token, User user) {
        Sprinkle sprinkle = Sprinkle.builder()
                .userId(user.getUserId())
                .roomId(user.getRoomId())
                .token(token.getToken())
                .amount(amount)
                .build();
        sprinkleRepository.save(sprinkle);
    }

    /**
     * 3자리 랜덤 문자열 생성(uppercase, lowercase)
     *
     * @return      3자리 랜덤 문자열
     */
    private String generateToken() {
        return RandomStringUtils.randomAlphabetic(Constant.RANDOM_TOKEN_LENGTH);
    }
}
