package com.kakaopay.repository;

import com.kakaopay.entity.Receive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReceiveRepository extends JpaRepository<Receive, UUID> {
    /**
     * 뿌리기 정보에 대한 받기 유저 아이디가 있는지 체크
     *
     * @param sprinkleId        뿌리기 아이디
     * @param userId            받기 유저 아이디
     * @return                  참 거짓
     */
    Boolean existsReceiveBySprinkleIdAndUserId(UUID sprinkleId, String userId);

    /**
     * 뿌리기 아이디로 받기 정보리스트를 조회한다
     *
     * @param sprinkleId        뿌리기 아이디
     * @return                  받기 리스트 정보
     */
    List<Receive> findAllBySprinkleId(UUID sprinkleId);
}
