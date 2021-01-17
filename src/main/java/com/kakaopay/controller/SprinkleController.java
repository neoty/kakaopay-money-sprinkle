package com.kakaopay.controller;

import com.kakaopay.constant.Constant;
import com.kakaopay.constant.Message;
import com.kakaopay.dto.Token;
import com.kakaopay.dto.reponse.ApiResponse;
import com.kakaopay.dto.request.SprinkleSubmit;
import com.kakaopay.dto.request.User;
import com.kakaopay.service.SprinkleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

@RestController
@RequiredArgsConstructor
@Validated
public class SprinkleController {
    private final SprinkleService sprinkleService;

    /**
     * 뿌리기
     *
     * @param userId            뿌리기 유저 아이디
     * @param roomId            뿌리기 대화방 아이디
     * @param sprinkleSubmit    뿌리기 요청 정보
     * @return 토큰
     */
    @PostMapping
    ApiResponse<Token> sprinkleSubmit(
            @RequestHeader(Constant.HEADER_USER_ID) @Pattern(regexp = Constant.REGEX_USER_ID, message = Message.INVALID_USER_ID) String userId,
            @RequestHeader(Constant.HEADER_ROOM_ID) @Pattern(regexp = Constant.REGEX_ROOM_ID, message = Message.INVALID_ROOM_ID) String roomId,
            @RequestBody SprinkleSubmit sprinkleSubmit) {

        User user = new User(userId);
        user.setRoomId(roomId);

        Token token = sprinkleService.sprinkleAndGenerateToken(user, sprinkleSubmit);
        return ApiResponse.success(token, Message.SPRINKLE_SUCCESS);
    }


}
