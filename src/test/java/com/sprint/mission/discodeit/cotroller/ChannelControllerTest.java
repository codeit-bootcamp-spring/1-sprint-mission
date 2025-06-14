package com.sprint.mission.discodeit.cotroller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.controller.ChannelController;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelModificationNotAllowedException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
public class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ChannelService channelService;

//  @BeforeEach
//  void setUp() {
//    channelPublicRequest = new ChannelPublicRequest("testChannel", "Channel test");
//    channelId = UUID.randomUUID();
//
//    channelDto = ChannelDto.builder()
//        .id(channelId)
//        .type(ChannelType.PUBLIC)
//        .name("testChannel")
//        .description("Channel test")
//        .build();
//
//    given(channelService.createPublicChannel(any(ChannelPublicRequest.class))).willReturn(
//        channelDto);
//  }

  @Test
  @DisplayName("POST /api/channels/public - 성공")
  void createPublicChannel_Success() throws Exception {
    ChannelPublicRequest createRequest = new ChannelPublicRequest(
        "test-channel",
        "채널 설명입니다."
    );

    UUID channelId = UUID.randomUUID();
    ChannelDto channelDto = ChannelDto.builder()
        .id(channelId)
        .type(ChannelType.PUBLIC)
        .name("test-channel")
        .description("채널 설명입니다.")
        .build();

    given(channelService.createPublicChannel(any(ChannelPublicRequest.class)))
        .willReturn(channelDto);

    mockMvc.perform(
            post("/api/channels/public")
                .content(new ObjectMapper().writeValueAsString(createRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.type").value("PUBLIC"))
        .andExpect(jsonPath("$.name").value("test-channel"))
        .andExpect(jsonPath("$.description").value("채널 설명입니다."));
  }

  @Test
  @DisplayName("POST /api/channels/public - 실패 : 유효하지 않은 요청")
  void createPublicChannel_Fail() throws Exception {
    ChannelPublicRequest invalidRequest = new ChannelPublicRequest("", "Channel test");
    mockMvc.perform(
        post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(invalidRequest))
    ).andExpect(status().isBadRequest());
  }


  @Test
  @DisplayName("POST /api/channels/private 비공개 채널 생성 성공 테스트")
  void createPrivateChannel_Success() throws Exception {
    // Given
    List<UUID> participantIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    ChannelPrivateRequest createRequest = new ChannelPrivateRequest(participantIds);

    UUID channelId = UUID.randomUUID();
    List<UserDto> participants = new ArrayList<>();
    for (UUID userId : participantIds) {
      participants.add(new UserDto(userId, "user-" + userId.toString().substring(0, 5),
          "user" + userId.toString().substring(0, 5) + "@example.com", null, false));
    }

    ChannelDto createdChannel = new ChannelDto(
        channelId,
        ChannelType.PRIVATE,
        null,
        null,
        participants,
        Optional.ofNullable(Instant.now())
    );

    given(channelService.createPrivateChannel(any(ChannelPrivateRequest.class)))
        .willReturn(createdChannel);

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.type").value("PRIVATE"))
        .andExpect(jsonPath("$.participants").isArray())
        .andExpect(jsonPath("$.participants.length()").value(2));
  }

  @Test
  @DisplayName("PATCH 공개 채널 업데이트 성공 테스트")
  void updateChannel_Success() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(
        "updated-channel",
        "업데이트된 채널 설명입니다."
    );

    ChannelDto updatedChannel = new ChannelDto(
        channelId,
        ChannelType.PUBLIC,
        "updated-channel",
        "업데이트된 채널 설명입니다.",
        new ArrayList<>(),
        Optional.ofNullable(Instant.now())
    );

    given(channelService.updateChannel(eq(channelId), any(ChannelUpdateRequest.class)))
        .willReturn(updatedChannel);

    // When & Then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("updated-channel"))
        .andExpect(jsonPath("$.description").value("업데이트된 채널 설명입니다."));
  }

  @Test
  @DisplayName("PATCH 채널 업데이트 실패 테스트 - 존재하지 않는 채널")
  void updateChannel_Failure_ChannelNotFound() throws Exception {
    // Given
    UUID nonExistentChannelId = UUID.randomUUID();
    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(
        "updated-channel",
        "업데이트된 채널 설명입니다."
    );

    given(channelService.updateChannel(eq(nonExistentChannelId),
        any(ChannelUpdateRequest.class)))
        .willThrow(
            new ChannelNotFoundException(Map.of("nonExistentChannelId", nonExistentChannelId)));

    // When & Then
    mockMvc.perform(patch("/api/channels/{channelId}", nonExistentChannelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("PATCH 채널 업데이트 실패 테스트 - 비공개 채널 업데이트 시도")
  void updateChannel_Failure_PrivateChannelUpdate() throws Exception {
    // Given
    UUID privateChannelId = UUID.randomUUID();
    ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(
        "updated-channel",
        "업데이트된 채널 설명입니다."
    );

    given(channelService.updateChannel(eq(privateChannelId), any(ChannelUpdateRequest.class)))
        .willThrow(new ChannelModificationNotAllowedException(
            Map.of("privateChannelId", privateChannelId)));

    // When & Then
    mockMvc.perform(patch("/api/channels/{channelId}", privateChannelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("DELETE 채널 삭제 성공 테스트")
  void deleteChannel_Success() throws Exception {
    // Given
    UUID channelId = UUID.randomUUID();
    willDoNothing().given(channelService).deleteChannelById(channelId);

    // When & Then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("DELETE 채널 삭제 실패 테스트 - 존재하지 않는 채널")
  void deleteChannel_Failure_ChannelNotFound() throws Exception {
    // Given
    UUID nonExistentChannelId = UUID.randomUUID();
    willThrow(new ChannelNotFoundException(Map.of("nonExistentChannelId", nonExistentChannelId)))
        .given(channelService).deleteChannelById(nonExistentChannelId);

    // When & Then
    mockMvc.perform(delete("/api/channels/{channelId}", nonExistentChannelId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET 사용자별 채널 목록 조회 성공 테스트")
  void findAllByUserId_Success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UUID channelId1 = UUID.randomUUID();
    UUID channelId2 = UUID.randomUUID();

    List<ChannelDto> channels = List.of(
        new ChannelDto(
            channelId1,
            ChannelType.PUBLIC,
            "public-channel",
            "공개 채널 설명",
            new ArrayList<>(),
            Optional.ofNullable(Instant.now())
        ),
        new ChannelDto(
            channelId2,
            ChannelType.PRIVATE,
            null,
            null,
            List.of(new UserDto(userId, "user1", "user1@example.com", null, true)),
            Optional.ofNullable(Instant.now().minusSeconds(3600))
        )
    );

    given(channelService.findAllByUserId(userId)).willReturn(channels);

    // When & Then
    mockMvc.perform(get("/api/channels")
            .param("userId", userId.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(channelId1.toString()))
        .andExpect(jsonPath("$[0].type").value("PUBLIC"))
        .andExpect(jsonPath("$[0].name").value("public-channel"))
        .andExpect(jsonPath("$[1].id").value(channelId2.toString()))
        .andExpect(jsonPath("$[1].type").value("PRIVATE"));
  }


}
