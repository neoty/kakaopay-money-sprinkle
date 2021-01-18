package com.kakaopay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.ApplicationTest;
import com.kakaopay.Utf8Config;
import com.kakaopay.dto.request.SprinkleSubmit;
import com.kakaopay.dto.request.User;
import com.kakaopay.repository.ReceiveRepository;
import com.kakaopay.repository.SprinkleRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Utf8Config
@SpringBootTest
@AutoConfigureMockMvc
class SprinkleReceiveControllerTest extends ApplicationTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private SprinkleRepository sprinkleRepository;

    @Autowired
    private ReceiveRepository receiveRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        sprinkleRepository.deleteAllInBatch();
        receiveRepository.deleteAllInBatch();
    }


    @Test
    void sprinkleSubmit() throws Exception {
        // given
        SprinkleSubmit sprinkleSubmit = new SprinkleSubmit();
        sprinkleSubmit.setAmount(10000);
        sprinkleSubmit.setNumber(10);
        User user = new User(RandomStringUtils.randomNumeric(10));
        user.setRoomId(RandomStringUtils.randomAlphabetic(10));

        // test
        MvcResult actions = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sprinkleSubmit))
                .header("X-USER-ID", user.getUserId())
                .header("X-ROOM-ID", user.getRoomId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    void sprinkleSubmit_valid() throws Exception {
        // given
        SprinkleSubmit sprinkleSubmit = new SprinkleSubmit();
        sprinkleSubmit.setAmount(10000);
        sprinkleSubmit.setNumber(10);
        User user = new User(RandomStringUtils.randomNumeric(10));
        user.setRoomId("");

        // test
        MvcResult actions = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sprinkleSubmit))
                .header("X-USER-ID", user.getUserId())
                .header("X-ROOM-ID", user.getRoomId()))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}