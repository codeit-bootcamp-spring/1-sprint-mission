package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.AuthRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.util.List;
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
public class ChannelIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mvc;

  @Test
  @Transactional
  void 회원가입_로그인_공개채널생성_성공() throws Exception {
    // 1. 회원가입
    var signupRequest = new UserCreateRequestDto("홍길동", "hong@test.com", "1234");

    MockMultipartFile userCreateJson = new MockMultipartFile(
        "userCreateRequest", "", "application/json",
        objectMapper.writeValueAsBytes(signupRequest)
    );

    MockMultipartFile profile = new MockMultipartFile(
        "profile", "profile.png", "image/png", "dummy".getBytes()
    );

    mvc.perform(multipart("/api/users")
            .file(userCreateJson)
            .file(profile)
            .with(req -> {
              req.setMethod("POST");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated());

    // 2. 로그인
    var loginRequest = new AuthRequestDto("홍길동", "1234");

    MvcResult loginResult = mvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andReturn();

    String response = loginResult.getResponse().getContentAsString();
    UserDto loginUser = objectMapper.readValue(response, UserDto.class);
    UUID userId = loginUser.getId();

    // 3. 공개 채널 생성
    var channelRequest = new PublicChannelCreateRequestDto("공지사항", "채널 설명입니다.");

    mvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("공지사항"))
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.description").value("채널 설명입니다."));

    //4. 비공개 채널 생성
    var privateChannelRequest = new PrivateChannelCreateRequestDto(List.of(userId));

    mvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(privateChannelRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.type").value("PRIVATE"));
  }

  @Test
  @Transactional
  void 공개채널_2_생성_조회_1수정_조회_2삭제_조회_() throws Exception {
    // 채널 1 생성
    PublicChannelCreateRequestDto req1 = new PublicChannelCreateRequestDto("channel-1", "desc-1");
    MvcResult result1 = mvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req1)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("channel-1"))
        .andReturn();

    ChannelDto created1 = objectMapper.readValue(result1.getResponse().getContentAsString(),
        ChannelDto.class);

    // 채널 2 생성
    PublicChannelCreateRequestDto req2 = new PublicChannelCreateRequestDto("channel-2", "desc-2");
    MvcResult result2 = mvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req2)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("channel-2"))
        .andReturn();

    ChannelDto created2 = objectMapper.readValue(result2.getResponse().getContentAsString(),
        ChannelDto.class);

    // 전체 조회 (userId는 임시로 채워둠)
    mvc.perform(get("/api/channels")
            .param("userId", UUID.randomUUID().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));

    // 채널 1 수정
    ChannelUpdateRequestDto updateRequest = new ChannelUpdateRequestDto("channel-1-edited",
        "desc-1-edited");
    mvc.perform(patch("/api/channels/" + created1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("channel-1-edited"));

    // 다시 조회
    mvc.perform(get("/api/channels")
            .param("userId", UUID.randomUUID().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));

    // 채널 2 삭제
    mvc.perform(delete("/api/channels/" + created2.getId()))
        .andExpect(status().isNoContent());

    // 최종 조회 - 1개 남았는지 확인
    mvc.perform(get("/api/channels")
            .param("userId", UUID.randomUUID().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }
}
