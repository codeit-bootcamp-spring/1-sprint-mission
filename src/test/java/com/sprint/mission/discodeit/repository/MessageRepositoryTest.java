package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ChannelRepository channelRepository;

  // 유저 생성 유틸 메서드
  private User createUser(String name, String email) {
    BinaryContent profile = new BinaryContent("profile.png", 2048L, "image/png");
    User user = new User(name, email, "secret", profile);
    new UserStatus(user, Instant.now());
    return userRepository.save(user);
  }

  // 채널 생성 유틸 메서드
  private Channel createChannel() {
    return channelRepository.save(new Channel(ChannelType.PUBLIC, "Channel1", "Test channel"));
  }

  // 메시지 생성 유틸 메서드
  private Message createMessage(Channel channel, User user) {
    Message msg = new Message("Hello", channel, user, List.of());
    return messageRepository.save(msg);
  }

  @Test
  @DisplayName("findAllByChannelIdWithAuthor - 성공 케이스")
  void testFindAllByChannelIdWithAuthorSuccess() {
    // given: 채널과 사용자, 메시지 2개 생성
    Channel channel = createChannel();
    User user = createUser("testuser", "test@me.com");

    Instant before = Instant.now();
    createMessage(channel, user);
    createMessage(channel, user);

    // when: 생성된 시간보다 1초 뒤 기준으로 메시지 페이징 조회
    var result = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(),
        before.plus(1, ChronoUnit.SECONDS),
        PageRequest.of(0, 10)
    );

    // then: 메시지 2개가 조회됨
    assertThat(result.getContent()).hasSize(2);
  }


  @Test
  @DisplayName("findAllByChannelIdWithAuthor - 실패 케이스 (존재하지 않는 채널)")
  void testFindAllByChannelIdWithAuthorFail() {
    // when: 임의의 UUID로 조회
    var result = messageRepository.findAllByChannelIdWithAuthor(UUID.randomUUID(), Instant.now(),
        PageRequest.of(0, 10));

    // then
    assertThat(result.getContent()).isEmpty();
  }

  @Test
  @DisplayName("findLastMessageAtByChannelId - 성공 케이스")
  void testFindLastMessageAtByChannelIdSuccess() {
    // given: 채널, 유저, 메시지 생성
    Channel channel = createChannel();
    User user = createUser("lastuser", "last@me.com");
    Message message = createMessage(channel, user);

    // when: 해당 채널의 마지막 메시지 시간 조회
    Optional<Instant> found = messageRepository.findLastMessageAtByChannelId(channel.getId());

    // then: 저장된 메시지의 시간과 같아야 함 (밀리초 정밀도로 비교)
    assertThat(found).isPresent();
    assertThat(found.get().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(message.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
  }

  @Test
  @DisplayName("findLastMessageAtByChannelId - 실패 케이스 (메시지 없음)")
  void testFindLastMessageAtByChannelIdFail() {
    // when: 메시지가 없는 채널에 대해 조회
    Optional<Instant> found = messageRepository.findLastMessageAtByChannelId(UUID.randomUUID());

    // then: 값이 없어야 함
    assertThat(found).isEmpty();
  }

  @Test
  @DisplayName("deleteAllByChannelId - 삭제 케이스")
  void testDeleteAllByChannelId() {
    // given: 채널에 메시지를 생성함
    Channel channel = createChannel();
    User user = createUser("deluser", "del@me.com");
    createMessage(channel, user);

    // when: 해당 채널의 메시지 전체 삭제
    messageRepository.deleteAllByChannelId(channel.getId());
    List<Message> remaining = messageRepository.findAll();

    // then: 메시지가 존재하지 않아야 함
    assertThat(remaining).isEmpty();
  }
}