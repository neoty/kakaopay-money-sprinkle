package com.kakaopay.constant;

import lombok.Getter;

@Getter
public class Constant {
    public static final String HEADER_USER_ID = "X-USER-ID";
    public static final String HEADER_ROOM_ID = "X-ROOM-ID";

    public static final String REGEX_USER_ID = "^[0-9]{1,100}$";
    public static final String REGEX_ROOM_ID = "^[a-zA-Z]{10}$";
    public static final String REGEX_TOKEN = "^[a-zA-Z]{3}$";

    public static final byte RANDOM_TOKEN_LENGTH = 3;
    public static final byte SPRINKLE_LOOKUP_MAX_DAY = 7;
    public static final int SPRINKLE_RECEIVE_EXPIRE_SECOND = 600;

    public static final int MIN_SPRINKLE = 1;
    public static final int MAX_SPRINKLE_AMOUNT = 100000000;
    public static final int MAX_SPRINKLE_NUMBER = 10000;
}
