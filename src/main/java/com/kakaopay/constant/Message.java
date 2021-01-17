package com.kakaopay.constant;

import lombok.Getter;

@Getter
public class Message {
    public static final String SPRINKLE_SUCCESS = "뿌리기가 성공하였습니다.";
    public static final String RECEIVE_SUCCESS = "받기를 성공하였습니다.";
    public static final String LOOKUP_SUCCESS = "조회에 성공하였습니다.";

    public static final String INVALID_USER_ID = "유효하지 않는 유저 아이디 형식입니다.";
    public static final String INVALID_ROOM_ID = "유요하지 않는 채팅방 아이디 형식입니다.";
    public static final String INVALID_TOKEN_ID = "유효하지 않는 토큰형식 입니다.";
    public static final String INVALID_SPRINKLE_REQUEST = "유효하지 않은 뿌리기 요청입니다.";
    public static final String INVALID_RECEIVE_REQUEST = "유효하지 않은 받기 요청입니다.";
    public static final String TOKEN_DUPLICATE_RETRY = "중복 토큰입니다. 재시도해 주세요.";
    public static final String DUPLICATE_RECEIVE_REQUEST = "중복 받기 요청입니다.";
    public static final String NOT_FOUND_SPRINKLE = "뿌리기 정보를 찾을수 없습니다.";

    public static final String INVALID_REQUEST = "유효하지 않은 요청입니다.";
    public static final String APPLICATION_ERROR = "시스템 오류입니다.";
}
