package com.kakaopay.exception.business;

import com.kakaopay.exception.Exception;

public class InvalidReceiveRequestException extends BusinessException {
    public InvalidReceiveRequestException() {
        super(Exception.INVALID_RECEIVE_REQUEST);
    }
}
