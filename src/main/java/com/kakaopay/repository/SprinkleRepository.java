package com.kakaopay.repository;

import com.kakaopay.entity.Sprinkle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SprinkleRepository extends JpaRepository<Sprinkle, UUID> {
    /**
     * 뿌리기정보 존재 체크 (token, user_id, created_at)
     *
     * @param token             뿌리기 토큰
     * @param userId            뿌리기 신청유저 아이디
     * @param beforeDateTime    이후 시간 조회
     * @return                  참 거짓
     */
    Boolean existsByTokenAndUserIdAndCreatedAtAfter(String token, String userId, LocalDateTime beforeDateTime);

    /**
     * 뿌리기 정보 조회(token, created_at)
     *
     * @param token             뿌리기 토큰
     * @param userId            뿌리기 신청유저 아이디
     * @param beforeDateTime    이후 시간 조회
     * @return                  뿌리기 정보
     */
    Optional<Sprinkle> findTop1ByTokenAndUserIdAndCreatedAtAfter(String token, String userId, LocalDateTime beforeDateTime);

    /**
     * 뿌리기 정보 조회(token, room_id, created_at)
     *
     * @param token             뿌리기 토큰
     * @param roomId            뿌리기 대화방 아이디
     * @param beforeDateTime    이후 시간 조회
     * @return                  뿌리기 정보
     */
    Optional<Sprinkle> findTop1ByTokenAndRoomIdAndCreatedAtAfter(String token, String roomId, LocalDateTime beforeDateTime);
}
