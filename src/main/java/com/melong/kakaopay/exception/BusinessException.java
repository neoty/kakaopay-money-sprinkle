package com.melong.kakaopay.exception;

import lombok.Getter;

@Getter
class BusinessException extends RuntimeException {
    private Exception exception;
    BusinessException(Exception exception) {
        super("message");
        this.exception = exception;
    }
}
