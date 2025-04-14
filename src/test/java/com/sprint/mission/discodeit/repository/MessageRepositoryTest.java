package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

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
  private EntityManager em;

  @Test
  void existsById() {
    User user = userRepository.save(new User("홍길동", "hong@naver.com", "1234", null));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "공개 채널", "테스트"));
    Message message = messageRepository.save(new Message("hello", channel, user, List.of()));

    em.flush();
    em.clear();

    boolean exists = messageRepository.existsById(message.getId());
    assertTrue(exists);
  }

  @Test
  void 존재하지_않는_메시지_테스트() {
    UUID messageId = UUID.randomUUID();
    boolean exists = messageRepository.existsById(messageId);
    assertFalse(exists);
  }

  @Test
  void findFirstMessages() {
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "채널", null));
    User user = userRepository.save(new User("홍길동", "hong@example.com", "1234", null));

    for (int i = 0; i < 5; i++) {
      messageRepository.save(new Message("메시지 " + i, channel, user, List.of()));
    }

    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"));

    em.flush();
    em.clear();

    Slice<Message> result = messageRepository.findFirstMessages(channel.getId(), pageable);

    assertThat(result).hasSize(3);
    assertThat(result.hasNext()).isTrue();
    assertThat(result.getContent().get(0).getAuthor().getUsername()).isEqualTo("홍길동");
  }

  @Test
  void deleteByChannelId() {
    User user = userRepository.save(new User("홍길동", "hong@naver.com", "1234", null));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "공개 채널", "테스트"));
    Message message = messageRepository.save(new Message("hello", channel, user, List.of()));

    em.flush();
    em.clear();

    messageRepository.deleteByChannelId(channel.getId());

    Optional<Message> result = messageRepository.findById(message.getId());
    assertThat(result).isEmpty();
  }

  @Test
  void findNextMessages() throws InterruptedException {
    User user = userRepository.save(new User("홍길동", "hong@naver.com", "1234", null));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "공개 채널", "테스트"));

    List<Message> saved = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Message message = messageRepository.save(new Message("메시지 " + i, channel, user, List.of()));
      saved.add(message);
      Thread.sleep(1000);
    }

    em.flush();
    em.clear();
    Message foundMessage = em.find(Message.class, saved.get(0).getId());
    Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
    Instant cursor = saved.get(2).getCreatedAt();
    Slice<Message> result = messageRepository.findNextMessages(channel.getId(), cursor, pageable);

    List<String> contents = result.getContent().stream()
        .map(Message::getContent)
        .toList();

    assertThat(contents).containsExactly("메시지 1", "메시지 0");
    assertThat(result.hasNext()).isFalse();

    //연관 데이터
    Message m0 = result.getContent().get(0);
    assertThat(m0.getAuthor()).isNotNull();
    assertThat(m0.getAuthor().getUsername()).isEqualTo("홍길동");
    assertThat(m0.getAuthor().getProfile()).isNull();
    assertThat(m0.getAttachmentIds()).isEmpty();
  }

  @Test
  void findFirstMessages_존재하지_않는_채널이면_빈결과() {
    UUID invalidChannelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"));

    Slice<Message> result = messageRepository.findFirstMessages(invalidChannelId, pageable);

    assertThat(result).isEmpty();
    assertThat(result.hasNext()).isFalse();
  }

  @Test
  void findNextMessages_커서보다_이전_메시지가_없으면_빈결과() throws InterruptedException {
    User user = userRepository.save(new User("홍길동", "hong@naver.com", "1234", null));
    Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, "공개 채널", "테스트"));

    Message message = messageRepository.save(new Message("메시지 0", channel, user, List.of()));
    Thread.sleep(5);

    em.flush();
    em.clear();

    // 커서를 message보다 더 이전 시점으로 설정
    Instant cursor = message.getCreatedAt().minusMillis(1000);

    Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

    Slice<Message> result = messageRepository.findNextMessages(channel.getId(), cursor, pageable);

    assertThat(result).isEmpty();
    assertThat(result.hasNext()).isFalse();
  }


  @Test
  void findNextMessages_존재하지_않는_채널이면_빈결과() {
    UUID invalidChannelId = UUID.randomUUID();
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));

    Slice<Message> result = messageRepository.findNextMessages(invalidChannelId, now, pageable);

    assertThat(result).isEmpty();
    assertThat(result.hasNext()).isFalse();
  }

  @Test
  void 존재하진_않는_ID_삭제() {
    UUID channelId = UUID.randomUUID();
    assertThatCode(() -> messageRepository.deleteByChannelId(channelId)).doesNotThrowAnyException();
  }
}