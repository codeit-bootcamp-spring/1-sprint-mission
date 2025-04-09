package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.JpaConfig;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.p6spy.P6spyConfig;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql("/data.sql")
@DataJpaTest
@Import({JpaConfig.class, P6spyConfig.class})
@ActiveProfiles("test")
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private TestEntityManager em;

  UUID channelId = UUID.fromString("10000000-0000-0000-0000-000000000001");

  @Test
  @DisplayName("findByChannel_Id - 31 messages, 페이지내이션 10개, 총 4페이지")
  void findByChannelId_pagination_success() {
    // given
    UUID channelId = UUID.fromString("10000000-0000-0000-0000-000000000001");
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));

    // when
    Page<Message> result = messageRepository.findByChannel_Id(channelId, pageable);

    // then
    assertThat(result.getContent()).hasSize(10);
    assertThat(result.getTotalElements()).isEqualTo(31);
    assertThat(result.getTotalPages()).isEqualTo(4);
  }

  @Test
  @DisplayName("findByChannel_Id - 정렬 조건에 의해 정렬되어 있다")
  void findByChannelId_paginationIsSortedCorrectly_success() {
    // given

    Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));

    // when
    Page<Message> result = messageRepository.findByChannel_Id(channelId, pageable);
    List<Instant> timestamps = result.getContent().stream()
        .map(Message::getCreatedAt)
        .toList();

    List<Instant> sorted = new ArrayList<>(timestamps);
    sorted.sort(Comparator.reverseOrder());

    assertThat(timestamps)
        .as("메시지는 createdAt 기준 DESC로 정렬되어 있어야 한다")
        .isEqualTo(sorted);
  }


  @Test
  @DisplayName("findByChannel_Id - 존재하지 않을 경우 빈 페이지")
  void findChannelById_shouldReturnEmptyPage() {
    UUID id = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);

    Page<Message> result = messageRepository.findByChannel_Id(id, pageable);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("findTopByChannel_IdOrderByCreatedAtDesc - 가장 최신 메시지 1개")
  void findTopByChannelId_success() {
    // when
    Optional<Message> result = messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channelId);

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getContent()).isEqualTo("Message 1");
  }

  @Test
  @DisplayName("findTopByChannel_IdOrderByCreatedAtDesc - 메시지 없는 채널")
  void findTopByChannelId_empty_failure() {
    UUID id = UUID.randomUUID();

    Optional<Message> result = messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(id);

    // then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("findByChannel_IdAndCreatedAtLessThan - 특정 시간 이전 메시지 존재")
  void findBeforeCreatedAt_success() {
    Instant time = Instant.now().plusSeconds(10);
    Pageable pageable = PageRequest.of(0, 10);

    // when
    Page<Message> result = messageRepository.findByChannel_IdAndCreatedAtLessThan(channelId, time,
        pageable);

    // then
    assertThat(result.getContent()).hasSize(10);
    assertThat(result.getTotalPages()).isEqualTo(4);
  }

  @Test
  @DisplayName("findByChannel_IdAndCreatedAtLessThan - EPOCH 조회시 빈 리스트")
  void findBeforeCreatedAt_fail() {
    Instant time = Instant.EPOCH;
    Pageable pageable = PageRequest.of(0, 10);

    // when
    Page<Message> result = messageRepository.findByChannel_IdAndCreatedAtLessThan(channelId, time,
        pageable);

    // then
    assertThat(result.getContent()).hasSize(0);
  }

  @Test
  @DisplayName("findLatestMessagesForEachChannel - 채널별 가장 최신 메시지")
  void findLatestMessagesForEachChannel_success() {
    // given
    UUID channelId2 = UUID.fromString("10000000-0000-0000-0000-000000000002");

    // when
    List<Message> result = messageRepository.findLatestMessagesForEachChannel(
        List.of(channelId, channelId2));
    List<String> messageContents = result.stream().map(m -> m.getContent())
        .collect(Collectors.toList());

    // then
    assertThat(result).hasSize(2);
    assertThat(messageContents).containsExactlyInAnyOrder(
        "Channel 2 Message 1",
        "Message 1"
    );
  }

  @Test
  @DisplayName("findLatestMessagesForEachChannel - 메시지 없는 채널은 빈 리스트 반환")
  void findLatestMessagesForEachChannel_fail() {
    // given
    UUID id = UUID.fromString("10000000-0000-0000-0000-000000000003");

    // when
    List<Message> result = messageRepository.findLatestMessagesForEachChannel(List.of(id));

    //then
    assertThat(result).isEmpty();
  }

}
