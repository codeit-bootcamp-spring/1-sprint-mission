package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

/**
 * ChannelRepository 슬라이스 테스트
 */
@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private TestEntityManager entityManager;

  /**
   * TestFixture: 채널 생성용 테스트 픽스처
   */
  private Channel createTestChannel(ChannelType type, String name) {
    Channel channel = new Channel(type, name, "설명: " + name);
    return channelRepository.save(channel);
  }

  @Test
  @DisplayName("타입이 PUBLIC이거나 ID 목록에 포함된 채널을 모두 조회할 수 있다")
  void findAllByTypeOrIdIn_ReturnsChannels() {
    // given
    Channel publicChannel1 = createTestChannel(ChannelType.PUBLIC, "공개채널1");
    Channel publicChannel2 = createTestChannel(ChannelType.PUBLIC, "공개채널2");
    Channel privateChannel1 = createTestChannel(ChannelType.PRIVATE, "비공개채널1");
    Channel privateChannel2 = createTestChannel(ChannelType.PRIVATE, "비공개채널2");

    channelRepository.saveAll(
        Arrays.asList(publicChannel1, publicChannel2, privateChannel1, privateChannel2));

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    List<UUID> selectedPrivateIds = List.of(privateChannel1.getId());
    List<Channel> foundChannels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        selectedPrivateIds);

    // then
    assertThat(foundChannels).hasSize(3); // 공개채널 2개 + 선택된 비공개채널 1개

    // 공개 채널 2개가 모두 포함되어 있는지 확인
    assertThat(
        foundChannels.stream().filter(c -> c.getType() == ChannelType.PUBLIC).count()).isEqualTo(2);

    // 선택된 비공개 채널만 포함되어 있는지 확인
    List<Channel> privateChannels = foundChannels.stream()
        .filter(c -> c.getType() == ChannelType.PRIVATE)
        .toList();
    assertThat(privateChannels).hasSize(1);
    assertThat(privateChannels.get(0).getId()).isEqualTo(privateChannel1.getId());
  }

  @Test
  @DisplayName("타입이 PUBLIC이 아니고 ID 목록이 비어있으면 비어있는 리스트를 반환한다")
  void findAllByTypeOrIdIn_EmptyList_ReturnsEmptyList() {
    // given
    Channel privateChannel1 = createTestChannel(ChannelType.PRIVATE, "비공개채널1");
    Channel privateChannel2 = createTestChannel(ChannelType.PRIVATE, "비공개채널2");

    channelRepository.saveAll(Arrays.asList(privateChannel1, privateChannel2));

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    List<Channel> foundChannels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of());

    // then
    assertThat(foundChannels).isEmpty();
  }
} 