package com.kakaopay.exception;

import com.kakaopay.constant.Message;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Exception {
    INVALID_USER_ID_(HttpStatus.UNAUTHORIZED, Message.INVALID_USER_ID),
    INVALID_ROOM_ID(HttpStatus.UNAUTHORIZED, Message.INVALID_ROOM_ID),
    INVALID_SPRINKLE_REQUEST(HttpStatus.BAD_REQUEST, Message.INVALID_SPRINKLE_REQUEST),
    INVALID_RECEIVE_REQUEST(HttpStatus.BAD_REQUEST, Message.INVALID_RECEIVE_REQUEST),
    TOKEN_DUPLICATE_RETRY(HttpStatus.CONFLICT, Message.TOKEN_DUPLICATE_RETRY),
    NOT_FOUND_SPRINKLE(HttpStatus.NOT_FOUND, Message.NOT_FOUND_SPRINKLE),
    DUPLICATE_RECEIVE_REQUEST(HttpStatus.CONFLICT, Message.DUPLICATE_RECEIVE_REQUEST),
    ;

    private HttpStatus httpStatus;
    private String message;
    Exception(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
