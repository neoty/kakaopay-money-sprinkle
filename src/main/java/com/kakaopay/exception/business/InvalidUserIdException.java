package com.kakaopay.exception.business;

import com.kakaopay.exception.Exception;

public class InvalidUserIdException extends BusinessException {
    public InvalidUserIdException() { super(Exception.INVALID_USER_ID_); }
}
