package com.melong.kakaopay.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Exception {
    USER_ID_VALIDATION(HttpStatus.UNAUTHORIZED, "USER_ID_VALIDATION"),
    ROOM_ID_VALIDATION(HttpStatus.UNAUTHORIZED, "ROOM_ID_VALIDATION"),
    SPRINKLE_VALIDATION(HttpStatus.BAD_REQUEST, "SPRINKLE_VALIDATION"),
    TOKEN_DUPLICATE(HttpStatus.CONFLICT, "keyduplicate");

    private HttpStatus httpStatus;
    private String message;
    Exception(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
