package com.melong.kakaopay.service;

import com.melong.kakaopay.exception.RoomIdValidationException;
import com.melong.kakaopay.exception.UserIdValidationException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
class UserValidationService {
    void checkUserIdOrElseThrows(String userId) {
        if (! Pattern.matches("^[0-9]*$", userId)) {
            throw new UserIdValidationException();
        }
    }

    void checkRoomIdOrElseThrows(String roomId) {
        if (! Pattern.matches("^[a-zA-Z-0-9]{3}$", roomId)) {
            throw new RoomIdValidationException();
        }
    }


}
