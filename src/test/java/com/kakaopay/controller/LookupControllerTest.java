package com.kakaopay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.ApplicationTest;
import com.kakaopay.repository.SprinkleRepository;
import com.kakaopay.Utf8Config;
import com.kakaopay.constant.Constant;
import com.kakaopay.dto.reponse.ApiResponse;
import com.kakaopay.entity.Sprinkle;
import com.kakaopay.repository.ReceiveRepository;
import com.kakaopay.repository.SprinkleRedisRepository;
import com.kakaopay.service.DistributionService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Utf8Config
@SpringBootTest
@AutoConfigureMockMvc
class LookupControllerTest extends ApplicationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    DistributionService distributionService;

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
    void lookup() throws Exception {
        // given
        final String sprinkleUserId = RandomStringUtils.randomNumeric(11);
        final String roomId = RandomStringUtils.randomAlphabetic(10);
        final String token = RandomStringUtils.randomAlphabetic(3);
        List<Integer> list = new ArrayList<>();
        list.add(300);
        list.add(700);
        sprinkleRedisRepository.saveAmountAndSetExpireSecond(sprinkleUserId + token, list, Constant.SPRINKLE_RECEIVE_EXPIRE_SECOND);
        sprinkleRepository.save(Sprinkle.builder().amount(1000).roomId(roomId).token(token).userId(sprinkleUserId).build());

        // when
        mockMvc.perform(put("/" + token)
                .header("X-USER-ID", RandomStringUtils.randomNumeric(10))
                .header("X-ROOM-ID", roomId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // when
        mockMvc.perform(put("/" + token)
                .header("X-USER-ID", RandomStringUtils.randomNumeric(9))
                .header("X-ROOM-ID", roomId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // when (내가 뿌린것일때)
        MvcResult actions = mockMvc.perform(get("/" + token)
                .header("X-USER-ID", sprinkleUserId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse apiResponse = objectMapper.readValue(actions.getResponse().getContentAsString(), ApiResponse.class);
        assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(apiResponse.getData()).isNotNull();


        // when (내가 생성한 뿌리기가 아닐때)
        MvcResult actions1 = mockMvc.perform(get("/" + token)
                .header("X-USER-ID", "ANOTHERUSER"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        ApiResponse apiResponse1 = objectMapper.readValue(actions1.getResponse().getContentAsString(), ApiResponse.class);
        assertThat(apiResponse1.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(apiResponse1.getData()).isNull();
    }
}