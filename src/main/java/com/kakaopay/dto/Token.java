package com.kakaopay.dto;

import com.kakaopay.constant.Constant;
import com.kakaopay.constant.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;


@AllArgsConstructor
@Getter
public class Token {
    @Pattern(regexp = Constant.REGEX_TOKEN, message = Message.INVALID_TOKEN_ID)
    private String token;
}
