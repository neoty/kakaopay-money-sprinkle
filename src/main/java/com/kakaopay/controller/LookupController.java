package com.kakaopay.controller;

import com.kakaopay.constant.Constant;
import com.kakaopay.constant.Message;
import com.kakaopay.dto.Token;
import com.kakaopay.dto.request.User;
import com.kakaopay.dto.reponse.ApiResponse;
import com.kakaopay.dto.reponse.Lookup;
import com.kakaopay.service.LookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequiredArgsConstructor
@Validated
public class LookupController {
    private final LookupService lookupService;

    /**
     * 뿌리기 정보 조회
     *
     * @param userId    뿌리기 사람 유저 아이디
     * @param token     뿌리기 토큰정보
     * @return          응답
     */
    @GetMapping(path = "/{token}")
    ApiResponse<Lookup> lookup(
            @RequestHeader(Constant.HEADER_USER_ID) @Pattern(regexp = Constant.REGEX_USER_ID, message = Message.INVALID_USER_ID) String userId,
            @PathVariable("token") @Valid Token token) {

        Lookup lookup = lookupService.getSprinkleStats(new User(userId), token);

        return ApiResponse.success(lookup, Message.LOOKUP_SUCCESS);
    }
}
