package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("채널의 모든 메세지 읽기 - 성공")
  void findAllByChannelIdWithAuthorSuccess() {
    BinaryContent profile = binaryContentRepository.save(
        new BinaryContent("img001.jpeg", 1024L, "jpeg"));
    User user = userRepository.save(
        new User("yubin", "yubin@codeit.com", "1234", profile));
    userStatusRepository.save(new UserStatus(user, Instant.now()));

    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "공지", null));

    Message message1 = new Message("메시지1", channel, user, null);
    Message message2 = new Message("메시지2", channel, user, null);
    messageRepository.saveAll(List.of(message1, message2));
    entityManager.flush();

    Pageable pageable = PageRequest.of(0, 10);

    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
        Instant.now(),
        pageable);

    assertEquals(2, result.getNumberOfElements());
    List<String> contents = result.getContent().stream().map(Message::getContent).toList();
    assertTrue(contents.contains("메시지1"));
    assertTrue(contents.contains("메시지2"));

    for (Message message : result.getContent()) {
      assertNotNull(message.getAuthor());
      assertNotNull(message.getAuthor().getStatus());
      assertNotNull(message.getAuthor().getProfile());
    }
  }

  @Test
  @DisplayName("채널의 모든 메세지 읽기 - 실패 (createdAt 이전 메세지가 없음)")
  void findAllByChannelIdWithAuthorFail() {
    User user = userRepository.save(new User("yubin", "yubin@codeit.com", "1234", null));
    userStatusRepository.save(new UserStatus(user, Instant.now()));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "공지", null));
    Message msg = messageRepository.save(new Message("미래 메시지", channel, user, null));

    Pageable pageable = PageRequest.of(0, 10);
    Instant earlier = Instant.now().minusSeconds(120);

    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(channel.getId(), earlier,
        pageable);

    assertTrue(result.isEmpty());
  }

  @Test
  @DisplayName("채널의 최근 메시지 시간 조회 - 성공")
  void findLastMessageAtByChannelIdSuccess() {
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "공지", null));
    User user = userRepository.save(new User("yubin", "yubin@codeit.com", "1234", null));
    userStatusRepository.save(new UserStatus(user, Instant.now()));

    Message msg1 = new Message("첫 번째 메시지", channel, user, null);
    Message msg2 = new Message("두 번째 메시지", channel, user, null);
    messageRepository.save(msg1);
    messageRepository.save(msg2);
    entityManager.flush();
    entityManager.clear();

    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());
    assertTrue(lastMessageAt.isPresent());

    Message latest = messageRepository.findById(msg2.getId()).orElseThrow();
    assertEquals(latest.getCreatedAt(), lastMessageAt.get());
  }

  @Test
  @DisplayName("채널의 최근 메시지 시간 조회 - 실패 (존재하지 않는 채널)")
  void findLastMessageAtByChannelIdFail_ChannelNotFound() {
    UUID invalidChannelId = UUID.randomUUID();
    Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(invalidChannelId);
    assertTrue(result.isEmpty());
  }
}
