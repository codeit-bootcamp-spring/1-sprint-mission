package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

class ChannelApiIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("공개 채널 생성 성공")
    void createPublicChannelSuccess() throws Exception {
        // Given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("general");

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("general"))
            .andExpect(jsonPath("$.type").value("PUBLIC"))
            .andReturn();

        String response = result.getResponse().getContentAsString();
        ChannelDto responseDto = objectMapper.readValue(response, ChannelDto.class);

        // Then
        Channel savedChannel = channelRepository.findById(responseDto.id()).orElseThrow();
        assertThat(savedChannel.getName()).isEqualTo("general");
        assertThat(savedChannel.getType()).isEqualTo(ChannelType.PUBLIC);
    }

    @Test
    @DisplayName("비공개 채널 생성 성공")
    void createPrivateChannelSuccess() throws Exception {
        // Given
        User user = new User("testuser", "test@email.com", "password123!", null);
        userRepository.save(user);

        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(user.getId());

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.type").value("PRIVATE"))
            .andReturn();

        String response = result.getResponse().getContentAsString();
        ChannelDto responseDto = objectMapper.readValue(response, ChannelDto.class);

        // Then
        Channel savedChannel = channelRepository.findById(responseDto.id()).orElseThrow();
        assertThat(savedChannel.getType()).isEqualTo(ChannelType.PRIVATE);
        assertThat(savedChannel.getUsers()).contains(user);
    }

    @Test
    @DisplayName("채널 정보 수정 성공")
    void updateChannelSuccess() throws Exception {
        // Given
        Channel channel = new Channel("general", ChannelType.PUBLIC);
        channelRepository.save(channel);

        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("new-general");

        // When
        mockMvc.perform(put("/api/v1/channels/{channelId}", channel.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("new-general"));

        // Then
        Channel updatedChannel = channelRepository.findById(channel.getId()).orElseThrow();
        assertThat(updatedChannel.getName()).isEqualTo("new-general");
    }

    @Test
    @DisplayName("채널 삭제 성공")
    void deleteChannelSuccess() throws Exception {
        // Given
        Channel channel = new Channel("general", ChannelType.PUBLIC);
        channelRepository.save(channel);

        // When
        mockMvc.perform(delete("/api/v1/channels/{channelId}", channel.getId()))
            .andExpect(status().isNoContent());

        // Then
        assertThat(channelRepository.findById(channel.getId())).isEmpty();
    }
} 