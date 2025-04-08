package com.sprint.mission.discodeit.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.helper.ChannelTestFactory;
import com.sprint.mission.discodeit.helper.MessageTestFactory;
import com.sprint.mission.discodeit.helper.UserTestFactory;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;


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

  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("채널로 메시지를 모두 조회할 수 있다.")
  void findAllByChannel() {
    // given
    User author = userRepository.save(UserTestFactory.create("user1", "u1@test.com", "pw"));
    Channel channel = channelRepository.save(ChannelTestFactory.create("테스트채널", "설명"));

    messageRepository.save(MessageTestFactory.create("첫 메시지", author, channel));
    messageRepository.save(MessageTestFactory.create("두 번째 메시지", author, channel));

    em.flush();
    em.clear();

    // when
    List<Message> result = messageRepository.findAllByChannel(channel);

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getContent()).isEqualTo("첫 메시지");
  }

  @Test
  @DisplayName("채널 ID로 메시지를 페이지 단위로 조회할 수 있다. (EntityGraph 포함)")
  void findAllByChannelId_withPagination() {
    // given
    User author = userRepository.save(UserTestFactory.create("user1", "user1@test.com", "pw"));
    Channel channel = channelRepository.save(ChannelTestFactory.create("채널", "내용"));

    for (int i = 1; i <= 5; i++) {
      messageRepository.save(MessageTestFactory.create("메시지 " + i, author, channel));
    }

    Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt"));

    em.flush();
    em.clear();

    // when
    Page<Message> page = messageRepository.findAllByChannel_Id(channel.getId(), pageable);

    // then
    assertThat(page.getContent()).hasSize(3);
    assertThat(page.getContent().get(0).getAuthor()).isNotNull(); // EntityGraph 확인
  }

  @Test
  @DisplayName("커서 기반으로 특정 시간 이전의 메시지를 조회할 수 있다.")
  void findAllByChannelIdAndCreatedAtBefore() {
    // given
    User author = userRepository.save(UserTestFactory.create("user3", "user3@test.com", "pw"));
    Channel channel = channelRepository.save(ChannelTestFactory.create("채널", "설명"));

    Instant now = Instant.now();

    Message old = MessageTestFactory.create("이전 메시지", author, channel);
    Message recent = MessageTestFactory.create("최근 메시지", author, channel);

    messageRepository.saveAll(List.of(old, recent));
    em.flush();
    em.clear();

    // 직접 DB에 createdAt 수정 / @Modifying 메서드 호출하면 내부적으로 flush 수행
    messageRepository.forceUpdateCreatedAt(old.getId(), now.minusSeconds(100));
    em.clear();

    // when
    Page<Message> result = messageRepository.findAllByChannel_IdAndCreatedAtBefore(
        channel.getId(), now.minusSeconds(10), PageRequest.of(0, 10));

    // then
    assertThat(result).hasSize(1);
    assertThat(result.getContent().get(0).getContent()).isEqualTo("이전 메시지");
  }
}