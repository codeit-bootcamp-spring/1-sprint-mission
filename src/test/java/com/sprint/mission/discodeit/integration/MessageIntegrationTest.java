package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MessageIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    void 회원가입_로그인_채널_메시지_전송() throws Exception {
        // 1. 회원가입
        var signUpRequest = new UserCreateRequestDto("홍길동", "hong@test.com", "1234");
        MockMultipartFile jsonPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(signUpRequest));

        MockMultipartFile profile = new MockMultipartFile(
                "profile",
                "profile.png",
                "image/png",
                "dummy".getBytes());

        String userResponse = mvc.perform(multipart("/api/users")
                        .file(jsonPart)
                        .file(profile)
                        .with(req -> {
                            req.setMethod("POST");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UUID userId = UUID.fromString(JsonPath.read(userResponse, "$.id"));

        // 2. 로그인
        var loginRequest = new AuthRequestDto("홍길동", "1234");
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        // 3. 채널 생성
        var publicChannelRequest = new PublicChannelCreateRequestDto("test-channel", "테스트 채널입니다");
        MvcResult createChannelResult = mvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publicChannelRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test-channel"))
                .andReturn();

        String channelId = JsonPath.read(createChannelResult.getResponse().getContentAsString(),
                "$.id");

        // 4. 메시지 전송
        var messageRequest = new CreateMessageRequestDto("안녕하세요", UUID.fromString(channelId),
                userId);
        MockMultipartFile messageJson = new MockMultipartFile(
                "messageCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(messageRequest));

        mvc.perform(multipart("/api/messages")
                        .file(messageJson)
                        .with(req -> {
                            req.setMethod("POST");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("안녕하세요"));
    }

    @Test
    @Transactional
    void 메시지_생성_삭제_조회_테스트() throws Exception {

        //  사용자 회원가입
        MockMultipartFile userJson = new MockMultipartFile(
                "userCreateRequest", "", "application/json",
                objectMapper.writeValueAsBytes(
                        new UserCreateRequestDto("홍길동", "hong@test.com", "1234"))
        );
        MockMultipartFile profile = new MockMultipartFile(
                "profile", "profile.png", "image/png", "dummy".getBytes()
        );
        var userResult = mvc.perform(multipart("/api/users")
                        .file(userJson).file(profile).with(req -> {
                            req.setMethod("POST");
                            return req;
                        }))
                .andExpect(status().isCreated())
                .andReturn();

        String userId = JsonPath.read(userResult.getResponse().getContentAsString(), "$.id");
        // 1. 채널 생성
        var createChannelRequest = new PublicChannelCreateRequestDto("test-channel", "test desc");
        var channelResult = mvc.perform(post("/api/channels/public")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createChannelRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        String channelId = JsonPath.read(channelResult.getResponse().getContentAsString(), "$.id");

        // 2. 메시지 생성
        var createMessageRequest = new CreateMessageRequestDto("Hello!", UUID.fromString(channelId),
                UUID.fromString(userId));
        var messageResult = mvc.perform(multipart("/api/messages")
                        .file(new MockMultipartFile("messageCreateRequest", "", "application/json",
                                objectMapper.writeValueAsBytes(createMessageRequest)))
                        .with(req -> {
                            req.setMethod("POST");
                            return req;
                        }))
                .andExpect(status().isCreated())
                .andReturn();
        String messageId = JsonPath.read(messageResult.getResponse().getContentAsString(), "$.id");

        // 3. 메시지 삭제
        mvc.perform(delete("/api/messages/" + messageId))
                .andExpect(status().isNoContent());

        // 4. 메시지 조회
        mvc.perform(get("/api/messages")
                        .param("channelId", channelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }
}
