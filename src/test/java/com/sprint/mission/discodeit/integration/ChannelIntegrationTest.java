package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ChannelIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @Transactional
  void 공개채널_생성_성공() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지", null);
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("공지"));
  }

  @Test
  @Transactional
  void 개인채널_생성_성공() throws Exception {
    User user1 = userRepository.save(new User("user1", "user1@test.com", "1234", null));
    User user2 = userRepository.save(new User("user2", "user2@test.com", "1234", null));
    UserStatus userStatus1 = userStatusRepository.save(new UserStatus(user1, Instant.now()));
    UserStatus userStatus2 = userStatusRepository.save(new UserStatus(user2, Instant.now()));

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(user1.getId(), user2.getId()));

    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.type").value(ChannelType.PRIVATE.name()))
        .andExpect(
            jsonPath("$.participants[*].username", Matchers.containsInAnyOrder("user1", "user2")));
  }

  @Test
  @Transactional
  void 공개채널_수정_성공() throws Exception {
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "공지", null));
    UUID channelId = channel.getId();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("공지 채널", "공지 채널입니다.");

    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.description").value("공지 채널입니다."));
  }

  @Test
  @Transactional
  void 개인채널_삭제_성공() throws Exception {
    Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));
    UUID channelId = channel.getId();

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

  }
}
