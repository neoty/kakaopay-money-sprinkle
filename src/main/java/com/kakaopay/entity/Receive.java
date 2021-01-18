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
@Table(indexes = @Index(name = "sprinkle_id_index", columnList = "sprinkleId"), uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"sprinkleId", "userId"}
        )
})
public class Receive {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID sprinkleId;

    @Column(length = 100, nullable = false)
    private String userId;

    @Column(columnDefinition = "INT(1) UNSIGNED ", nullable = false)
    private Integer amount;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;
}
