package com.kakaopay.service;

import com.kakaopay.constant.Constant;
import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.User;
import com.kakaopay.exception.business.*;
import com.kakaopay.repository.ReceiveRepository;
import com.kakaopay.repository.SprinkleRepository;
import com.kakaopay.entity.Sprinkle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
class ValidationService {

    private final SprinkleRepository sprinkleRepository;
    private final ReceiveRepository receiveRepository;

    /**
     * X-USER-ID 형식 체크
     *
     * @param userId    유저 아이디
     */
    void checkUserIdOrElseThrows(String userId) {
        if (! Pattern.matches(Constant.REGEX_USER_ID, userId)) {
            throw new InvalidUserIdException();
        }
    }

    /**
     * X-ROOM-ID 형식 체크
     *
     * @param roomId    대화방 아이디
     */
    void checkRoomIdOrElseThrows(String roomId) {
        if (! Pattern.matches(Constant.REGEX_ROOM_ID, roomId)) {
            throw new InvalidRoomIdException();
        }
    }

    /**
     * 금액 분배 기본 체크
     *
     * @param amount                분배 금액
     * @param distributionNumber    분배 숫자
     */
    void checkDistributeAmountAndNumberOrElseThrow(int amount, int distributionNumber) {
        if (amount < 1 || distributionNumber < 1) {
            throw new InvalidSprinkleRequestException();
        }

        if (amount < distributionNumber) {
            throw new InvalidSprinkleRequestException();
        }
    }

    /**
     * 받기 가능여부 체크
     *
     * @param user      받을려는 유저 정보
     * @param sprinkle  받고자 하는 뿌리기 정보
     */
    void checkReceivableOrElseThrow(User user, Sprinkle sprinkle) {
        
        if (sprinkle.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND))) {
            
            throw new InvalidReceiveRequestException();
        }

        if (user.getUserId().equals(sprinkle.getUserId())) {
            
            throw new InvalidReceiveRequestException();
        }

        if (! user.getRoomId().equals(sprinkle.getRoomId())) {
            
            throw new InvalidReceiveRequestException();
        }

        if (receiveRepository.existsReceiveBySprinkleIdAndUserId(sprinkle.getId(), user.getUserId())) {
            
            throw new DuplicateReceiveRequestException();
        }
    }

    /**
     * 뿌리기 생성시 토큰 중복 여부 체크
     * 7일 이전 뿌리기 신청건에서 토큰과 뿌리기 신청 유저 아이디를 기준으로 확인
     *
     * @param token     토큰정보
     * @param userId    뿌리기 요청 유저 아이디
     */
    void checkTokenDuplicateOrThrow(Token token, String userId) {
        if (sprinkleRepository.existsByTokenAndUserIdAndCreatedAtAfter(
                token.getToken(),
                userId,
                LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY))) {

            throw new TokenDuplicateRetryException();
        }
    }
}
