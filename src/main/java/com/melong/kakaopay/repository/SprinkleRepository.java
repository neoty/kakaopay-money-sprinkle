package com.melong.kakaopay.repository;

import com.melong.kakaopay.entity.Sprinkle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SprinkleRepository extends JpaRepository<Sprinkle, UUID> {
    Boolean existsByTokenAndCreatedAtAfter(String token, LocalDateTime beforeDateTime);

    Optional<Sprinkle> findByTokenAndCreatedAtAfter(String token, LocalDateTime beforeDateTime);
}
