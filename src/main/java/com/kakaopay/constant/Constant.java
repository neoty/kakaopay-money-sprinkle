package com.kakaopay.constant;

import lombok.Getter;

@Getter
public class Constant {
    public static final String HEADER_USER_ID = "X-USER-ID";
    public static final String HEADER_ROOM_ID = "X-ROOM-ID";

    public static final String REGEX_USER_ID = "^[0-9]{1,255}$";
    public static final String REGEX_ROOM_ID = "^[a-zA-Z]{10}$";
    public static final String REGEX_TOKEN = "^[a-zA-Z]{3}$";

    public static final Integer RANDOM_TOKEN_LENGTH = 3;
    public static final Integer SPRINKLE_LOOKUP_MAX_DAY = 7;
    public static final Integer SPRINKLE_RECEIVE_EXPIRE_SECOND = 600;
}
