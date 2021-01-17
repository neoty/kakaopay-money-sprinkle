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

    @Column
    private String userId;

    @Column(columnDefinition = "BINARY(10)")
    private String roomId;

    @Column(columnDefinition = "BINARY(3)")
    private String token;

    @Column
    private Integer amount;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;
}
