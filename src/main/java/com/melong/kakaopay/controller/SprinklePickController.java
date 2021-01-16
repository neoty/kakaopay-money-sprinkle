package com.melong.kakaopay.controller;

import com.melong.kakaopay.constants.Message;
import com.melong.kakaopay.dto.*;
import com.melong.kakaopay.service.SprinklePickService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class SprinklePickController {
    private final SprinklePickService moneyService;

    @PostMapping
    ApiResponse<Token> sprinkle(
            @RequestHeader("X-USER-ID") @Pattern(regexp = "^[0-9]*$") String userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @RequestBody RequestSprinkle requestSprinkle) {

        Token token = moneyService.sprinkleAndGenerateToken(new User(userId, roomId), requestSprinkle);

        return ApiResponse.success(token, Message.SPRINKLE_SUCCESS);
    }

    @PutMapping(path = "/{token}")
    ApiResponse<PickDto.Amount> pick(
            @RequestHeader("X-USER-ID") @Pattern(regexp = "^[0-9]*$") String userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @PathVariable("token") @Pattern(regexp = "^[a-zA-Z-0-9]{3}$") String token) {

        PickDto.Amount pickAmount = moneyService.pickByToken(new User(userId, roomId), new Token(token));

        return ApiResponse.success(pickAmount, Message.PICK_SUCCESS);
    }


    @GetMapping(path = "/{token}")
    String lookup(
            @RequestHeader("X-USER-ID") @Pattern(regexp = "^[0-9]*$") String userId,
            @PathVariable("token") @Pattern(regexp = "^[a-zA-Z-0-9]{3}$") String token) {
        return token;
    }
}
