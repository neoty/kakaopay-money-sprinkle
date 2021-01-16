package com.melong.kakaopay.exception;

public class RoomIdValidationException extends BusinessException {
    public RoomIdValidationException() {
        super(Exception.ROOM_ID_VALIDATION);
    }
}
