package com.kakaopay.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Sprinkle {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(columnDefinition = "INT(1) UNSIGNED", nullable = false)
    private Integer amount;

    @Column(length = 100, nullable = false)
    private String userId;

    @Column(columnDefinition = "VARBINARY(10)", nullable = false)
    private String roomId;

    @Column(columnDefinition = "VARBINARY(3)", nullable = false)
    private String token;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;
}
