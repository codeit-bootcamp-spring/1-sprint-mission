package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

class MessageApiIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("메시지 생성 및 조회 성공")
    void createAndFindMessageSuccess() throws Exception {
        // Given
        User user = new User("testuser", "test@email.com", "password123!", null);
        userRepository.save(user);

        Channel channel = new Channel("general", ChannelType.PUBLIC);
        channelRepository.save(channel);

        MessageCreateRequest request = new MessageCreateRequest(channel.getId(), user.getId(), "Hello, world!");

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.content").value("Hello, world!"))
            .andReturn();

        String response = result.getResponse().getContentAsString();
        MessageDto responseDto = objectMapper.readValue(response, MessageDto.class);

        // Then
        Message savedMessage = messageRepository.findById(responseDto.id()).orElseThrow();
        assertThat(savedMessage.getContent()).isEqualTo("Hello, world!");
        assertThat(savedMessage.getUser()).isEqualTo(user);
        assertThat(savedMessage.getChannel()).isEqualTo(channel);
    }

    @Test
    @DisplayName("메시지 수정 성공")
    void updateMessageSuccess() throws Exception {
        // Given
        User user = new User("testuser", "test@email.com", "password123!", null);
        userRepository.save(user);

        Channel channel = new Channel("general", ChannelType.PUBLIC);
        channelRepository.save(channel);

        Message message = new Message(user, channel, "Original message");
        messageRepository.save(message);

        MessageUpdateRequest request = new MessageUpdateRequest("Updated message");

        // When
        mockMvc.perform(put("/api/v1/messages/{messageId}", message.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("Updated message"));

        // Then
        Message updatedMessage = messageRepository.findById(message.getId()).orElseThrow();
        assertThat(updatedMessage.getContent()).isEqualTo("Updated message");
    }

    @Test
    @DisplayName("채널별 메시지 목록 조회 성공")
    void findMessagesByChannelIdSuccess() throws Exception {
        // Given
        User user = new User("testuser", "test@email.com", "password123!", null);
        userRepository.save(user);

        Channel channel = new Channel("general", ChannelType.PUBLIC);
        channelRepository.save(channel);

        Message message1 = new Message(user, channel, "First message");
        Message message2 = new Message(user, channel, "Second message");
        messageRepository.saveAll(List.of(message1, message2));

        // When & Then
        mockMvc.perform(get("/api/v1/messages/channels/{channelId}", channel.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].content").value("First message"))
            .andExpect(jsonPath("$[1].content").value("Second message"));
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void deleteMessageSuccess() throws Exception {
        // Given
        User user = new User("testuser", "test@email.com", "password123!", null);
        userRepository.save(user);

        Channel channel = new Channel("general", ChannelType.PUBLIC);
        channelRepository.save(channel);

        Message message = new Message(user, channel, "Test message");
        messageRepository.save(message);

        // When
        mockMvc.perform(delete("/api/v1/messages/{messageId}", message.getId()))
            .andExpect(status().isNoContent());

        // Then
        assertThat(messageRepository.findById(message.getId())).isEmpty();
    }
} 