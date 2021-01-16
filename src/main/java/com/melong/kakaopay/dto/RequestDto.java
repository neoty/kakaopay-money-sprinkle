package com.melong.kakaopay.dto;

import lombok.Getter;
import lombok.Setter;

public class RequestDto {

    public static class Give {
        private Long amount;
        private Long number;
    }
}
