package com.kakaopay.exception.business;

import com.kakaopay.exception.Exception;

public class TokenDuplicateRetryException extends BusinessException {
    public TokenDuplicateRetryException() {
        super(Exception.TOKEN_DUPLICATE_RETRY);
    }
}
