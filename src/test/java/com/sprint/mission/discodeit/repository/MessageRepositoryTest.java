package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.entity.*;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestAuditingConfig.class)
public class MessageRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(MessageRepositoryTest.class);

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;

  private User createTestUser(String username, String email) {
    BinaryContent profile = new BinaryContent(username + "-profile.jpg", 1024L, "image/jpeg");
    binaryContentRepository.save(profile);
    User user = new User(username, email, "password123!@#", profile);
    userRepository.save(user);
    userStatusRepository.save(new UserStatus(user, Instant.now()));
    entityManager.flush();
    return user;
  }

  private Channel createTestChannel(ChannelType type, String name) {
    Channel channel = new Channel(type, name, "설명: " + name);
    channelRepository.save(channel);
    entityManager.flush();
    return channel;
  }

  private Message createTestMessage(String content, Channel channel, User author,
      Instant createdAt) {
    Message message = new Message(content, channel, author, new ArrayList<>());
    if (createdAt != null) {
      ReflectionTestUtils.setField(message, "createdAt", createdAt);
    }
    messageRepository.save(message);
    entityManager.flush();
    return message;
  }

  @Test
  @Order(1)
  @DisplayName("메시지를 저장하고 ID로 조회할 수 있다")
  void saveAndFindMessageById() {
    log.info("======== 메시지 저장 및 ID로 조회 테스트 시작 ========");

    User user = createTestUser("testuser", "test@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "general");
    String content = "테스트 메시지입니다.";
    Message savedMessage = createTestMessage(content, channel, user, null);

    Optional<Message> foundMessage = messageRepository.findById(savedMessage.getId());

    assertThat(foundMessage).isPresent();
    assertThat(foundMessage.get().getContent()).isEqualTo(content);
    assertThat(foundMessage.get().getChannel().getId()).isEqualTo(channel.getId());
    assertThat(foundMessage.get().getAuthor().getId()).isEqualTo(user.getId());

    log.info("======== 메시지 저장 및 ID로 조회 테스트 종료 ========");
  }

  @Test
  @Order(2)
  @DisplayName("채널 ID와 작성자로 메시지를 페이징 조회할 수 있다")
  void findMessagesByChannelIdWithAuthor() {
    log.info("======== 채널 ID와 작성자로 메시지 조회 테스트 시작 ========");

    User user = createTestUser("msguser", "msguser@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "message-channel");

    Instant now = Instant.now();
    createTestMessage("메시지 1", channel, user, now.minusSeconds(7200));
    createTestMessage("메시지 2", channel, user, now.minusSeconds(3600));
    createTestMessage("메시지 3", channel, user, now);

    Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        now.plusSeconds(1),
        PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    assertThat(messages.getContent()).hasSize(2);
    assertThat(messages.hasNext()).isTrue();
    assertThat(messages.getContent().get(0).getCreatedAt())
        .isAfterOrEqualTo(messages.getContent().get(1).getCreatedAt());
    assertThat(messages.getContent().get(0).getAuthor()).isNotNull();
    assertThat(messages.getContent().get(0).getAuthor().getStatus()).isNotNull();

    log.info("======== 채널 ID와 작성자로 메시지 조회 테스트 종료 ========");
  }

  @Test
  @Order(3)
  @DisplayName("메시지가 없는 채널은 빈 결과를 반환한다")
  void findEmptyChannelMessages() {
    log.info("======== 메시지가 없는 채널 조회 테스트 시작 ========");

    User user = createTestUser("emptyuser", "empty@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "empty-channel");
    Channel other = createTestChannel(ChannelType.PUBLIC, "other-channel");
    createTestMessage("다른 채널 메시지", other, user, Instant.now());

    Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        Instant.now().plusSeconds(1),
        PageRequest.of(0, 10)
    );

    assertThat(messages.getContent()).isEmpty();
    assertThat(messages.hasNext()).isFalse();

    log.info("======== 메시지가 없는 채널 조회 테스트 종료 ========");
  }

  @Test
  @Order(4)
  @DisplayName("채널의 마지막 메시지 시간을 조회할 수 있다")
  void findLastMessageAtByChannel() {
    log.info("======== 채널의 마지막 메시지 시간 조회 테스트 시작 ========");

    User user = createTestUser("lastuser", "last@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "last-message-channel");

    Instant now = Instant.now();
    createTestMessage("과거 메시지", channel, user, now.minusSeconds(7200));
    createTestMessage("중간 메시지", channel, user, now.minusSeconds(3600));
    Message last = createTestMessage("최신 메시지", channel, user, now);

    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());

    assertThat(lastMessageAt).isPresent();
    assertThat(lastMessageAt.get()).isEqualTo(last.getCreatedAt());

    log.info("======== 채널의 마지막 메시지 시간 조회 테스트 종료 ========");
  }

  @Test
  @Order(5)
  @DisplayName("메시지가 없는 채널의 마지막 메시지 시간은 비어 있다")
  void findLastMessageAtByEmptyChannel() {
    log.info("======== 메시지가 없는 채널의 마지막 메시지 시간 조회 테스트 시작 ========");

    Channel channel = createTestChannel(ChannelType.PUBLIC, "empty-last-channel");
    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());

    assertThat(lastMessageAt).isEmpty();

    log.info("======== 메시지가 없는 채널의 마지막 메시지 시간 조회 테스트 종료 ========");
  }

  @Test
  @Order(6)
  @DisplayName("채널 ID로 모든 메시지를 삭제할 수 있다")
  void deleteAllMessagesByChannelId() {
    log.info("======== 채널 ID로 모든 메시지 삭제 테스트 시작 ========");

    User user = createTestUser("deleteuser", "delete@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "channel-to-delete");
    createTestMessage("삭제될 메시지 1", channel, user, Instant.now().minusSeconds(3600));
    createTestMessage("삭제될 메시지 2", channel, user, Instant.now());

    Slice<Message> before = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        Instant.now().plusSeconds(1),
        PageRequest.of(0, 10)
    );
    assertThat(before.getContent()).hasSize(2);

    messageRepository.deleteAllByChannelId(channel.getId());
    entityManager.flush();
    entityManager.clear();

    Slice<Message> after = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        Instant.now().plusSeconds(1),
        PageRequest.of(0, 10)
    );

    assertThat(after.getContent()).isEmpty();

    log.info("======== 채널 ID로 모든 메시지 삭제 테스트 종료 ========");
  }

  @Test
  @Order(7)
  @DisplayName("페이징을 통해 메시지를 조회할 수 있다")
  void findMessagesWithPaging() {
    log.info("======== 페이징으로 메시지 조회 테스트 시작 ========");

    User user = createTestUser("pageuser", "page@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "paging-channel");

    for (int i = 0; i < 10; i++) {
      createTestMessage("페이징 메시지 " + i, channel, user, Instant.now().minusSeconds(i * 1000));
    }

    Slice<Message> first = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        Instant.now().plusSeconds(1),
        PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    assertThat(first.getContent()).hasSize(5);
    assertThat(first.hasNext()).isTrue();

    Instant lastTime = first.getContent().get(4).getCreatedAt();
    Slice<Message> second = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        lastTime,
        PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    assertThat(second.getContent()).hasSize(5);
    assertThat(second.hasNext()).isFalse();

    for (Message msg : first.getContent()) {
      assertThat(msg.getCreatedAt()).isAfterOrEqualTo(second.getContent().get(0).getCreatedAt());
    }

    log.info("======== 페이징으로 메시지 조회 테스트 종료 ========");
  }
}
