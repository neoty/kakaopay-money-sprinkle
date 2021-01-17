package com.kakaopay.controller;

import com.kakaopay.constant.Constant;
import com.kakaopay.constant.Message;
import com.kakaopay.dto.Amount;
import com.kakaopay.dto.Token;
import com.kakaopay.dto.reponse.ApiResponse;
import com.kakaopay.dto.request.User;
import com.kakaopay.service.ReceiverService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequiredArgsConstructor
@Validated
public class ReceiveController {
    private final ReceiverService receiverService;

    /**
     * 받기 요청
     *
     * @param userId    받기 유저 아이디
     * @param roomId    뿌리기 대화방 아이디
     * @param token     뿌리기 토큰 정보
     * @return          받은 금액
     */
    @PutMapping(path = "/{token}")
    ApiResponse<Amount> receiveSubmit(
            @RequestHeader(Constant.HEADER_USER_ID) @Pattern(regexp = Constant.REGEX_USER_ID, message = Message.INVALID_USER_ID) String userId,
            @RequestHeader(Constant.HEADER_ROOM_ID) @Pattern(regexp = Constant.REGEX_ROOM_ID, message = Message.INVALID_ROOM_ID) String roomId,
            @PathVariable("token") @Valid Token token) {

        User user = new User(userId);
        user.setRoomId(roomId);

        Amount receive = receiverService.receiveByToken(user, token);

        return ApiResponse.success(receive, Message.RECEIVE_SUCCESS);
    }
}
