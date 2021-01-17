package com.kakaopay.dto.reponse;

import com.kakaopay.entity.Receive;
import com.kakaopay.entity.Sprinkle;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Lookup {
    private LocalDateTime sprinkleTime;
    private Integer sprinkleAmount;
    private Integer totalReceivedAmount = 0;
    private List<ReceiveInformation> receiveInformation;

    @Getter
    @AllArgsConstructor
    public static class ReceiveInformation {
        private Integer receivedAmount;
        private String userId;
    }

    public Lookup(Sprinkle sprinkle, List<Receive> receives) {
        this.sprinkleTime = sprinkle.getCreatedAt();
        this.sprinkleAmount = sprinkle.getAmount();

        this.receiveInformation = receives.stream().map(receive -> {
            this.totalReceivedAmount += receive.getAmount();
            return new ReceiveInformation(receive.getAmount(), receive.getUserId());
        }).collect(Collectors.toList());
    }
}
