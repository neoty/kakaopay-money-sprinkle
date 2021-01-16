package com.melong.kakaopay.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Setter
@Getter
public class RequestSprinkle {
    @Min(1)
    private int amount;
    @Min(1)
    private int number;
}
