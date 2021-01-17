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
@Table(indexes = @Index(name = "sprinkle_id_index", columnList = "sprinkleId"))
public class Receive {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(columnDefinition = "BINARY(16)")
    private UUID sprinkleId;

    @Column
    private String userId;

    @Column
    private Integer amount;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;
}
