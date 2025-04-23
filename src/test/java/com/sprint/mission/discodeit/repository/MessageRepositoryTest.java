package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * MessageRepository 슬라이스 테스트
 */
@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  /**
   * TestFixture: 테스트용 사용자 생성
   */
  private User createTestUser(String username, String email) {
    BinaryContent profile = new BinaryContent("profile.jpg", 1024L, "image/jpeg");
    User user = new User(username, email, "password123!@#", profile);
    // UserStatus 생성 및 연결
    UserStatus status = new UserStatus(user, Instant.now());
    return userRepository.save(user);
  }

  /**
   * TestFixture: 테스트용 채널 생성
   */
  private Channel createTestChannel(ChannelType type, String name) {
    Channel channel = new Channel(type, name, "설명: " + name);
    return channelRepository.save(channel);
  }

  /**
   * TestFixture: 테스트용 메시지 생성 ReflectionTestUtils를 사용하여 createdAt 필드를 직접 설정
   */
  private Message createTestMessage(String content, Channel channel, User author,
      Instant createdAt) {
    Message message = new Message(content, channel, author, new ArrayList<>());

    // 생성 시간이 지정된 경우, ReflectionTestUtils로 설정
    if (createdAt != null) {
      ReflectionTestUtils.setField(message, "createdAt", createdAt);
    }

    Message savedMessage = messageRepository.save(message);
    entityManager.flush();

    return savedMessage;
  }

  @Test
  @DisplayName("채널 ID와 생성 시간으로 메시지를 페이징하여 조회할 수 있다")
  void findAllByChannelIdWithAuthor_ReturnsMessagesWithAuthor() {
    // given
    User user = createTestUser("testUser", "test@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "테스트채널");

    Instant now = Instant.now();
    Instant fiveMinutesAgo = now.minus(5, ChronoUnit.MINUTES);
    Instant tenMinutesAgo = now.minus(10, ChronoUnit.MINUTES);

    // 채널에 세 개의 메시지 생성 (시간 순서대로)
    Message message1 = createTestMessage("첫 번째 메시지", channel, user, tenMinutesAgo);
    Message message2 = createTestMessage("두 번째 메시지", channel, user, fiveMinutesAgo);
    Message message3 = createTestMessage("세 번째 메시지", channel, user, now);

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when - 최신 메시지보다 이전 시간으로 조회
    Slice<Message> messages = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        now.plus(1, ChronoUnit.MINUTES),  // 현재 시간보다 더 미래
        PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
    );

    // then
    assertThat(messages).isNotNull();
    assertThat(messages.hasContent()).isTrue();
    assertThat(messages.getNumberOfElements()).isEqualTo(2);  // 페이지 크기 만큼만 반환
    assertThat(messages.hasNext()).isTrue();

    // 시간 역순(최신순)으로 정렬되어 있는지 확인
    List<Message> content = messages.getContent();
    assertThat(content.get(0).getCreatedAt()).isAfterOrEqualTo(content.get(1).getCreatedAt());

    // 저자 정보가 함께 로드되었는지 확인 (FETCH JOIN)
    Message firstMessage = content.get(0);
    assertThat(Hibernate.isInitialized(firstMessage.getAuthor())).isTrue();
    assertThat(Hibernate.isInitialized(firstMessage.getAuthor().getStatus())).isTrue();
    assertThat(Hibernate.isInitialized(firstMessage.getAuthor().getProfile())).isTrue();
  }

  @Test
  @DisplayName("채널의 마지막 메시지 시간을 조회할 수 있다")
  void findLastMessageAtByChannelId_ReturnsLastMessageTime() {
    // given
    User user = createTestUser("testUser", "test@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "테스트채널");

    Instant now = Instant.now();
    Instant fiveMinutesAgo = now.minus(5, ChronoUnit.MINUTES);
    Instant tenMinutesAgo = now.minus(10, ChronoUnit.MINUTES);

    // 채널에 세 개의 메시지 생성 (시간 순서대로)
    createTestMessage("첫 번째 메시지", channel, user, tenMinutesAgo);
    createTestMessage("두 번째 메시지", channel, user, fiveMinutesAgo);
    Message lastMessage = createTestMessage("세 번째 메시지", channel, user, now);

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        channel.getId());

    // then
    assertThat(lastMessageAt).isPresent();
    // 마지막 메시지 시간과 일치하는지 확인 (밀리초 단위 이하의 차이는 무시)
    assertThat(lastMessageAt.get().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(lastMessage.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
  }

  @Test
  @DisplayName("메시지가 없는 채널에서는 마지막 메시지 시간이 없다")
  void findLastMessageAtByChannelId_NoMessages_ReturnsEmpty() {
    // given
    Channel emptyChannel = createTestChannel(ChannelType.PUBLIC, "빈채널");

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(
        emptyChannel.getId());

    // then
    assertThat(lastMessageAt).isEmpty();
  }

  @Test
  @DisplayName("채널의 모든 메시지를 삭제할 수 있다")
  void deleteAllByChannelId_DeletesAllMessages() {
    // given
    User user = createTestUser("testUser", "test@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "테스트채널");
    Channel otherChannel = createTestChannel(ChannelType.PUBLIC, "다른채널");

    // 테스트 채널에 메시지 3개 생성
    createTestMessage("첫 번째 메시지", channel, user, null);
    createTestMessage("두 번째 메시지", channel, user, null);
    createTestMessage("세 번째 메시지", channel, user, null);

    // 다른 채널에 메시지 1개 생성
    createTestMessage("다른 채널 메시지", otherChannel, user, null);

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    messageRepository.deleteAllByChannelId(channel.getId());
    entityManager.flush();
    entityManager.clear();

    // then
    // 해당 채널의 메시지는 삭제되었는지 확인
    List<Message> channelMessages = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(), 
        Instant.now().plus(1, ChronoUnit.DAYS), 
        PageRequest.of(0, 100)
    ).getContent();
    assertThat(channelMessages).isEmpty();

    // 다른 채널의 메시지는 그대로인지 확인
    List<Message> otherChannelMessages = messageRepository.findAllByChannelIdWithAuthor(
        otherChannel.getId(), 
        Instant.now().plus(1, ChronoUnit.DAYS),
        PageRequest.of(0, 100)
    ).getContent();
    assertThat(otherChannelMessages).hasSize(1);
  }
} 