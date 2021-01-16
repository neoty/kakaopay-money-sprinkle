package com.melong.kakaopay.exception;

public class TokenDuplicateException extends BusinessException {
    public TokenDuplicateException() {
        super(Exception.TOKEN_DUPLICATE);
    }
}
