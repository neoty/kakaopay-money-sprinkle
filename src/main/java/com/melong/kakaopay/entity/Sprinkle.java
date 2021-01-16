package com.melong.kakaopay.entity;

import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Table
public class Sprinkle {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column
    private String userId;

    @Column
    private String roomId;

    @Column
    private String token;

    @Column
    private Integer amount;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;
}
