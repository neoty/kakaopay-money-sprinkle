package com.melong.kakaopay.service;

import com.melong.kakaopay.dto.PickDto;
import com.melong.kakaopay.dto.RequestSprinkle;
import com.melong.kakaopay.dto.Token;
import com.melong.kakaopay.dto.User;
import com.melong.kakaopay.entity.Sprinkle;
import com.melong.kakaopay.exception.TokenDuplicateException;
import com.melong.kakaopay.repository.SprinkleRedisRepository;
import com.melong.kakaopay.repository.SprinkleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SprinklePickService {
    private final UserValidationService userValidationService;
    private final DistributionService distributionService;
    private final SprinkleRedisRepository sprinkleRedisRepository;
    private final SprinkleRepository sprinkleRepository;

    public Token sprinkleAndGenerateToken(User user, RequestSprinkle requestSprinkle) {
        userValidationService.checkRoomIdOrElseThrows(user.getRoomId());
        userValidationService.checkUserIdOrElseThrows(user.getUserId());

        Token token = new Token(generateToken());
        checkTokenDuplicateOrThrow(token);

        List<Integer> distributedAmount = distributionService.getDistributedAmount(requestSprinkle.getAmount(), requestSprinkle.getNumber());

        saveSprinkleHistory(requestSprinkle.getAmount(), token, user);
        saveSprinkleCache(token, distributedAmount);

        return token;
    }

    public PickDto.Amount pickByToken(User user, Token token) {
        userValidationService.checkRoomIdOrElseThrows(user.getRoomId());
        userValidationService.checkUserIdOrElseThrows(user.getUserId());

        Sprinkle sprinkle = getAvailableSprinkle(token).;


        return null;
    }

    private void checkPickableOrThrow(User user, Sprinkle sprinkle) {

    }



    private Sprinkle getAvailableSprinkle(Token token) {
        return sprinkleRepository.findByTokenAndCreatedAtAfter(token.getToken(), LocalDateTime.now().minusMinutes(10))
                .orElseThrow();
    }


    private void saveSprinkleCache(Token token, List<Integer> amount) {
        sprinkleRedisRepository.saveAmount(token.getToken(), amount);
    }

    private void saveSprinkleHistory(Integer amount, Token token, User user) {
        Sprinkle sprinkle = Sprinkle.builder()
                .userId(user.getUserId())
                .roomId(user.getRoomId())
                .token(token.getToken())
                .amount(amount)
                .build();
        sprinkleRepository.save(sprinkle);
    }

    private void checkTokenDuplicateOrThrow(Token token) {
        if (sprinkleRepository.existsByTokenAndCreatedAtAfter(
                token.getToken(),
                LocalDateTime.now().minusDays(7))) {

            throw new TokenDuplicateException();
        }
    }

    private String generateToken() {
        return RandomStringUtils.randomAlphanumeric(3);
    }
}
