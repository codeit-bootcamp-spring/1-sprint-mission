package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ChannelApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        channelRepository.deleteAll();
        userRepository.deleteAll();

        testUser1 = User.builder()
                .name("테스트 사용자 1")
                .email("user1@example.com")
                .password("password1")
                .build();
        userRepository.save(testUser1);

        testUser2 = User.builder()
                .name("테스트 사용자 2")
                .email("user2@example.com")
                .password("password2")
                .build();
        userRepository.save(testUser2);
    }

    @Test
    @DisplayName("공개 채널 생성 및 조회 통합 테스트")
    void createAndGetPublicChannel() throws Exception {
        // 1. 공개 채널 생성
        PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest();
        createRequest.setName("통합 테스트 공개 채널");
        createRequest.setDescription("통합 테스트 설명");

        MvcResult createResult = mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("통합 테스트 공개 채널"))
                .andExpect(jsonPath("$.description").value("통합 테스트 설명"))
                .andExpect(jsonPath("$.type").value("PUBLIC"))
                .andReturn();

        ChannelDto createdChannel = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ChannelDto.class
        );

        // 2. 생성된 채널 조회
        mockMvc.perform(get("/api/channels/{channelId}", createdChannel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdChannel.getId().toString()))
                .andExpect(jsonPath("$.name").value("통합 테스트 공개 채널"))
                .andExpect(jsonPath("$.description").value("통합 테스트 설명"))
                .andExpect(jsonPath("$.type").value("PUBLIC"));

        // 데이터베이스에서 채널 확인
        Channel savedChannel = channelRepository.findById(createdChannel.getId())
                .orElseThrow(() -> new AssertionError("채널이 저장되지 않았습니다."));
        
        assertEquals("통합 테스트 공개 채널", savedChannel.getName());
        assertEquals("통합 테스트 설명", savedChannel.getDescription());
        assertEquals(ChannelType.PUBLIC, savedChannel.getType());
    }

    @Test
    @DisplayName("비공개 채널 생성 및 참여자 조회 통합 테스트")
    void createAndGetPrivateChannel() throws Exception {
        // 1. 비공개 채널 생성
        PrivateChannelCreateRequest createRequest = new PrivateChannelCreateRequest();
        createRequest.setParticipantIds(Arrays.asList(testUser1.getId(), testUser2.getId()));

        MvcResult createResult = mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("PRIVATE"))
                .andExpect(jsonPath("$.participants", hasSize(2)))
                .andReturn();

        ChannelDto createdChannel = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                ChannelDto.class
        );

        // 2. 생성된 채널 조회
        mockMvc.perform(get("/api/channels/{channelId}", createdChannel.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdChannel.getId().toString()))
                .andExpect(jsonPath("$.type").value("PRIVATE"))
                .andExpect(jsonPath("$.participants", hasSize(2)))
                .andExpect(jsonPath("$.participants[0].id").isNotEmpty())
                .andExpect(jsonPath("$.participants[1].id").isNotEmpty());

        // 3. 사용자별 채널 목록 조회
        mockMvc.perform(get("/api/channels").param("userId", testUser1.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(createdChannel.getId().toString()));
    }

    @Test
    @DisplayName("채널 정보 수정 통합 테스트")
    void updateChannel() throws Exception {
        // 1. 채널 생성
        Channel channel = Channel.builder()
                .name("수정 전 채널명")
                .description("수정 전 설명")
                .type(ChannelType.PUBLIC)
                .build();
        
        Channel savedChannel = channelRepository.save(channel);

        // 2. 채널 정보 수정
        PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest();
        updateRequest.setNewName("수정 후 채널명");
        updateRequest.setNewDescription("수정 후 설명");

        mockMvc.perform(patch("/api/channels/{channelId}", savedChannel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("수정 후 채널명"))
                .andExpect(jsonPath("$.description").value("수정 후 설명"));

        // 3. 수정된 정보 확인
        Channel updatedChannel = channelRepository.findById(savedChannel.getId())
                .orElseThrow(() -> new AssertionError("채널이 존재하지 않습니다."));
        
        assertEquals("수정 후 채널명", updatedChannel.getName());
        assertEquals("수정 후 설명", updatedChannel.getDescription());
    }

    @Test
    @DisplayName("채널 삭제 통합 테스트")
    void deleteChannel() throws Exception {
        // 1. 채널 생성
        Channel channel = Channel.builder()
                .name("삭제할 채널")
                .description("삭제 테스트")
                .type(ChannelType.PUBLIC)
                .build();
        
        Channel savedChannel = channelRepository.save(channel);
        UUID channelId = savedChannel.getId();

        // 2. 채널 삭제
        mockMvc.perform(delete("/api/channels/{channelId}", channelId))
                .andExpect(status().isNoContent());

        // 3. 삭제 확인
        assertFalse(channelRepository.existsById(channelId));
    }

    @Test
    @DisplayName("존재하지 않는 채널 접근 시 실패 케이스 통합 테스트")
    void notFoundChannelTest() throws Exception {
        UUID nonExistentChannelId = UUID.randomUUID();

        // 1. 존재하지 않는 채널 조회
        mockMvc.perform(get("/api/channels/{channelId}", nonExistentChannelId))
                .andExpect(status().isNotFound());

        // 2. 존재하지 않는 채널 수정
        PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest();
        updateRequest.setNewName("존재하지 않는 채널");

        mockMvc.perform(patch("/api/channels/{channelId}", nonExistentChannelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        // 3. 존재하지 않는 채널 삭제
        mockMvc.perform(delete("/api/channels/{channelId}", nonExistentChannelId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("비공개 채널 생성 실패 케이스 - 참여자 없음")
    void createPrivateChannelWithoutParticipants() throws Exception {
        // 빈 참여자 목록으로 비공개 채널 생성 시도
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest();
        request.setParticipantIds(List.of());

        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
} 