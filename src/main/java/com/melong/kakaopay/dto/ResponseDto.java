package com.melong.kakaopay.dto;

import lombok.Getter;
import lombok.Setter;

public class ResponseDto {

    @Getter
    @Setter
    public static class Token {
        private String token;
    }

    @Getter
    @Setter
    public static class Take {
        private Long amount;
    }
}