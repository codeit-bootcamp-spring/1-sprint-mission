package com.sprint.mission.discodeit.service.slice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaAuditing
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private UserRepository userRepository;

  private Channel channel;
  private User author;
  private UserStatus userStatus;

  @BeforeEach
  void setup() {
    // 채널 및 사용자 생성
    channel = new Channel(ChannelType.PUBLIC, "name", "test");
    author = new User("username", "test@gmail.com", "password", null);
    userStatus = new UserStatus(author, Instant.now());
    channelRepository.save(channel);
    userRepository.save(author);
  }

  @Test
  void findAllByChannelIdWithAuthor_Success() {
    // given
    Message message1 = new Message("content1", channel, author, null);
    Message message2 = new Message("content2", channel, author, null);
    messageRepository.save(message1);
    messageRepository.save(message2);

    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthor(channel.getId(),
        Instant.now(), pageable);

    // then
    assertNotNull(messages);
    assertEquals(2, messages.getContent().size());
    assertTrue(messages.getContent().get(0).getCreatedAt()
        .isAfter(messages.getContent().get(1).getCreatedAt()));
  }

  @Test
  void findLastMessageAtByChannelId_Success() {
    // given
    Message message1 = new Message("content1", channel, author, null);
    Message message2 = new Message("content2", channel, author, null);
    messageRepository.save(message1);
    messageRepository.save(message2);

    // when
    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());

    // then
    assertNotNull(lastMessageAt);
  }

  @Test
  void deleteAllByChannelId_Success() {
    // given
    Message message1 = new Message("content1", channel, author, null);
    Message message2 = new Message("content2", channel, author, null);
    messageRepository.save(message1);
    messageRepository.save(message2);

    // when
    messageRepository.deleteAllByChannelId(channel.getId());

    // then
    List<Message> messages = messageRepository.findAll();

    assertEquals(messages.size(), 0);
  }
}

