package com.kakaopay.exception.business;

import com.kakaopay.exception.Exception;

public class DuplicateReceiveRequestException extends BusinessException {
    public DuplicateReceiveRequestException() {
        super(Exception.DUPLICATE_RECEIVE_REQUEST);
    }
}
