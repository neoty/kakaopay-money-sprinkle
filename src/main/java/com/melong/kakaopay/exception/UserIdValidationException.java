package com.melong.kakaopay.exception;

public class UserIdValidationException extends BusinessException {
    public UserIdValidationException() { super(Exception.USER_ID_VALIDATION); }
}
