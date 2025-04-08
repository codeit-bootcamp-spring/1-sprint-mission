package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class MessageIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private MessageRepository messageRepository;

  @Test
  @Transactional
  void 메세지_생성_성공() throws Exception {
    User user = userRepository.save(new User("yubin", "yubin@test.com", "1234", null));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "단체방", null));
    UserStatus userStatus = userStatusRepository.save(new UserStatus(user, Instant.now()));

    MessageCreateRequest request = new MessageCreateRequest("안녕", channel.getId(), user.getId());

    MockMultipartFile jsonPart = new MockMultipartFile(
        "messageCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(request)
    );

    MockMultipartFile attachment = new MockMultipartFile(
        "attachment",
        "profile.png",
        "image/png",
        "dummy".getBytes()
    );

    mockMvc.perform(multipart("/api/messages")
            .file(jsonPart)
            .file(attachment)
            .with(req -> {
              req.setMethod("POST");
              return req;
            })
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("안녕"))
        .andExpect(jsonPath("$.channelId").value(channel.getId().toString()))
        .andExpect(jsonPath("$.author.id").value(user.getId().toString()));
  }

  @Test
  @Transactional
  void 메세지_수정_성공() throws Exception {
    User user = userRepository.save(new User("yubin", "yubin@test.com", "1234", null));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "단체방", null));
    UserStatus userStatus = userStatusRepository.save(new UserStatus(user, Instant.now()));

    Message message = messageRepository.save(new Message("안녕", channel, user, null));
    MessageUpdateRequest request = new MessageUpdateRequest("안녕하세요");

    mockMvc.perform(patch("/api/messages/{messageId}", message.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("안녕하세요"));
  }

  @Test
  @Transactional
  void 메세지_삭제_성공() throws Exception {
    User user = userRepository.save(new User("yubin", "yubin@test.com", "1234", null));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "단체방", null));
    UserStatus userStatus = userStatusRepository.save(new UserStatus(user, Instant.now()));

    Message message = messageRepository.save(new Message("안녕", channel, user, null));

    mockMvc.perform(delete("/api/messages/{messageId}", message.getId()))
        .andExpect(status().isNoContent());
  }

  @Test
  @Transactional
  void 메세지_조회_성공() throws Exception {
    User user = userRepository.save(new User("yubin", "yubin@test.com", "1234", null));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "단체방", null));
    UserStatus userStatus = userStatusRepository.save(new UserStatus(user, Instant.now()));

    Message message1 = messageRepository.save(new Message("안녕", channel, user, null));
    Message message2 = messageRepository.save(new Message("안녕하세요", channel, user, null));

    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.getId().toString())
            .param("page", "0")
            .param("size", "10")
            .param("sort", "createdAt,desc")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[*].content", Matchers.containsInAnyOrder("안녕", "안녕하세요")));
  }
}
