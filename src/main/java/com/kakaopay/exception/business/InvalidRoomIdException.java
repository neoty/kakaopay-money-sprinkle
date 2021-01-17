package com.kakaopay.exception.business;

import com.kakaopay.exception.Exception;

public class InvalidRoomIdException extends BusinessException {
    public InvalidRoomIdException() {
        super(Exception.INVALID_ROOM_ID);
    }
}
