package com.kakaopay.service;

import com.kakaopay.constant.Constant;
import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.User;
import com.kakaopay.exception.business.NotFoundSprinkleException;
import com.kakaopay.repository.ReceiveRepository;
import com.kakaopay.repository.SprinkleRepository;
import com.kakaopay.dto.reponse.Lookup;
import com.kakaopay.entity.Receive;
import com.kakaopay.entity.Sprinkle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LookupService {
    private final ValidationService validationService;
    private final SprinkleRepository sprinkleRepository;
    private final ReceiveRepository receiveRepository;

    /**
     * 뿌리기 건에 대한 정보를 조회및 반환한다.
     *
     * @param user      뿌리기 신청건 유저 정보
     * @param token     토큰 정보
     * @return          조회 포맷
     */
    public Lookup getSprinkleStats(User user, Token token) {
        validationService.checkUserIdOrElseThrows(user.getUserId());

        Sprinkle sprinkle = findMySprinkleOrElseThrow(token, user.getUserId());
        List<Receive> receive = getReceiveBy(sprinkle.getId());

        return new Lookup(sprinkle, receive);
    }

    /**
     * 뿌리기 아이디로 받은 정보 리스트를 반환한다
     *
     * @param sprinkleId    뿌리기 아이디
     * @return              받은정보 리스트
     */
    private List<Receive> getReceiveBy(UUID sprinkleId) {
        return receiveRepository.findAllBySprinkleId(sprinkleId);
    }

    /**
     * 생성한 뿌리기 정보를 토큰과 요청자 아이디로 조회및 반환한다(7일전 이후 건에 대해)
     *
     * @param token     토큰정보
     * @param userId    조회 요청한 사용자 아이디
     * @return          뿌리기 정보
     */
    private Sprinkle findMySprinkleOrElseThrow(Token token, String userId) {

        return sprinkleRepository.findTop1ByTokenAndUserIdAndCreatedAtAfter(
                token.getToken(),
                userId,
                LocalDateTime.now().minusDays(Constant.SPRINKLE_LOOKUP_MAX_DAY))
                .orElseThrow(NotFoundSprinkleException::new);
    }
}
