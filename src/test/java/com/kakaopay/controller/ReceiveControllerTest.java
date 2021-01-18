package com.kakaopay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.ApplicationTest;
import com.kakaopay.constant.Message;
import com.kakaopay.repository.SprinkleRepository;
import com.kakaopay.Utf8Config;
import com.kakaopay.constant.Constant;
import com.kakaopay.dto.reponse.ApiResponse;
import com.kakaopay.entity.Sprinkle;
import com.kakaopay.repository.ReceiveRepository;
import com.kakaopay.repository.SprinkleRedisRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Utf8Config
@SpringBootTest
@AutoConfigureMockMvc
class ReceiveControllerTest extends ApplicationTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SprinkleRedisRepository sprinkleRedisRepository;

    @Autowired
    private SprinkleRepository sprinkleRepository;

    @Autowired
    private ReceiveRepository receiveRepository;

    @BeforeEach
    void setup() {
        sprinkleRepository.deleteAllInBatch();
        receiveRepository.deleteAllInBatch();
    }

    @Test
    void receiveSubmit_정상() throws Exception {
        // given
        final String sprinkleUserId = "000";
        final String receiveUserId = "111";
        final String roomId = RandomStringUtils.randomAlphabetic(10);
        final String token = RandomStringUtils.randomAlphabetic(3);
        List<Integer> list = new ArrayList<>();
        list.add(500);
        list.add(500);
        sprinkleRedisRepository.saveAmountAndSetExpireSecond(sprinkleUserId + token, list, Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND);
        sprinkleRepository.save(Sprinkle.builder().amount(1000).roomId(roomId).token(token).userId(sprinkleUserId).build());

        // when
        MvcResult actions = mockMvc.perform(put("/" + token)
                .header("X-USER-ID", receiveUserId)
                .header("X-ROOM-ID", roomId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse apiResponse = objectMapper.readValue(actions.getResponse().getContentAsString(), ApiResponse.class);
        assertThat(apiResponse.getMessage()).isEqualTo(Message.RECEIVE_SUCCESS);
    }

    @Test
    void receiveSubmit_중복받기_요청() throws Exception {
        // given
        final String sprinkleUserId = "000";
        final String receiveUserId = "111";
        final String roomId = RandomStringUtils.randomAlphabetic(10);
        final String token = RandomStringUtils.randomAlphabetic(3);
        List<Integer> list = new ArrayList<>();
        list.add(500);
        sprinkleRedisRepository.saveAmountAndSetExpireSecond(sprinkleUserId + token, list, Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND);
        sprinkleRepository.save(Sprinkle.builder().amount(1000).roomId(roomId).token(token).userId(sprinkleUserId).build());

        // when
        mockMvc.perform(put("/" + token)
                .header("X-USER-ID", receiveUserId)
                .header("X-ROOM-ID", roomId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // when
        MvcResult actions = mockMvc.perform(put("/" + token)
                .header("X-USER-ID", receiveUserId)
                .header("X-ROOM-ID", roomId))
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();

        // assert
        ApiResponse apiResponse = objectMapper.readValue(actions.getResponse().getContentAsString(), ApiResponse.class);
        assertThat(apiResponse.getMessage()).isEqualTo(Message.DUPLICATE_RECEIVE_REQUEST);
    }

}