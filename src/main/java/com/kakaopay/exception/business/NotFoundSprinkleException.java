package com.kakaopay.exception.business;

import com.kakaopay.exception.Exception;

public class NotFoundSprinkleException extends BusinessException {
    public NotFoundSprinkleException() { super(Exception.NOT_FOUND_SPRINKLE); }
}
