package com.kakaopay.dto.request;

import com.kakaopay.constant.Constant;
import com.kakaopay.constant.Message;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Setter
@Getter
public class SprinkleSubmit {
    @Min(value = Constant.MIN_SPRINKLE, message = Message.INVALID_MIN_SPRINKLE)
    @Max(value = Constant.MAX_SPRINKLE_AMOUNT, message = Message.INVALID_MAX_SPRINKLE_AMOUNT)
    @NotNull
    private int amount;

    @Min(value = Constant.MIN_SPRINKLE, message = Message.INVALID_MIN_SPRINKLE)
    @Max(value = Constant.MAX_SPRINKLE_NUMBER, message = Message.INVALID_MAX_SPRINKLE_NUMBER)
    @NotNull
    private int number;
}
