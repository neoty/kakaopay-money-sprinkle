package com.kakaopay.exception.business;

import com.kakaopay.exception.Exception;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private Exception exception;
    BusinessException(Exception exception) {
        super(exception.getMessage());
        this.exception = exception;
    }
}
