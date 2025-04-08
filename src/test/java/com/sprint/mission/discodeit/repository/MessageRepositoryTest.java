package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.domain.Slice;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChannelRepository channelRepository;

  private User user;
  private Channel channel;

  @BeforeEach
  void setUp() {
    // 테스트용 User, Channel 생성 및 저장
    user = userRepository.save(new User("testUser", "testUser@example.com", "password", null));
    channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "testChannel", "testDesc"));
  }

  @Test
  @DisplayName("메시지 저장 후 ID 조회 성공 테스트")
  void testSaveAndFindById_Success() {
    // given
    Message saved = messageRepository.save(
        new Message("Hello World", channel, user, Collections.emptyList())
    );

    // when
    Optional<Message> found = messageRepository.findById(saved.getId());

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getContent()).isEqualTo("Hello World");
    assertThat(found.get().getChannel().getId()).isEqualTo(channel.getId());
    assertThat(found.get().getAuthor().getId()).isEqualTo(user.getId());
  }

  @Test
  @DisplayName("존재하지 않는 메시지 ID 조회 실패 테스트")
  void testFindById_Fail() {
    // given
    UUID nonExistentId = UUID.randomUUID();

    // when
    Optional<Message> found = messageRepository.findById(nonExistentId);

    // then
    assertThat(found).isEmpty();
  }

  @Test
  @DisplayName("findLastMessageAtByChannelId 실패 테스트 (메시지가 없는 경우 Optional empty 반환)")
  void testFindLastMessageAtByChannelId_Fail_NoMessages() {
    // given

    // when
    Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(channel.getId());

    // then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("deleteAllByChannelId 성공 테스트")
  void testDeleteAllByChannelId_Success() {
    // given
    messageRepository.save(new Message("1", channel, user, Collections.emptyList()));
    messageRepository.save(new Message("2", channel, user, Collections.emptyList()));
    assertThat(messageRepository.count()).isEqualTo(2);

    // when
    messageRepository.deleteAllByChannelId(channel.getId());

    // then
    assertThat(messageRepository.count()).isZero();
  }
}
