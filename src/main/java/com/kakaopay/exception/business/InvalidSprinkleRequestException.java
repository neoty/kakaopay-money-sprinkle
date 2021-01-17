package com.kakaopay.exception.business;

import com.kakaopay.exception.Exception;

public class InvalidSprinkleRequestException extends BusinessException {
    public InvalidSprinkleRequestException() { super(Exception.INVALID_SPRINKLE_REQUEST); }
}
