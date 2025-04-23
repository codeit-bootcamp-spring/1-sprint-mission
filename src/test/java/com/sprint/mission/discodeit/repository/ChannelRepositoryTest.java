package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestAuditingConfig.class)
public class ChannelRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(ChannelRepositoryTest.class);

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ChannelRepository channelRepository;

  private Channel createTestChannel(ChannelType type, String name) {
    Channel channel = new Channel(type, name, "설명: " + name);
    Channel savedChannel = channelRepository.save(channel);
    entityManager.flush();
    return savedChannel;
  }

  @Test
  @Order(1)
  @DisplayName("채널을 저장하고 ID로 조회할 수 있다")
  void saveAndFindById() {
    log.info("======== 채널 저장 및 ID로 조회 테스트 시작 ========");

    String name = "general";
    ChannelType type = ChannelType.PUBLIC;
    Channel savedChannel = createTestChannel(type, name);
    Optional<Channel> foundChannel = channelRepository.findById(savedChannel.getId());

    assertThat(foundChannel).isPresent();
    assertThat(foundChannel.get().getName()).isEqualTo(name);
    assertThat(foundChannel.get().getType()).isEqualTo(type);

    log.info("======== 채널 저장 및 ID로 조회 테스트 종료 ========");
  }

  @Test
  @Order(2)
  @DisplayName("모든 채널을 조회할 수 있다")
  void findAllChannels() {
    log.info("======== 모든 채널 조회 테스트 시작 ========");

    createTestChannel(ChannelType.PUBLIC, "channel1");
    createTestChannel(ChannelType.PRIVATE, "channel2");
    createTestChannel(ChannelType.PUBLIC, "channel3");

    List<Channel> channels = channelRepository.findAll();

    assertThat(channels).hasSize(3);
    assertThat(channels).extracting("name").contains("channel1", "channel2", "channel3");

    log.info("======== 모든 채널 조회 테스트 종료 ========");
  }

  @Test
  @Order(3)
  @DisplayName("타입이 PUBLIC이거나 ID 목록에 포함된 채널을 모두 조회할 수 있다")
  void findAllByTypeOrIdIn_ReturnsChannels() {
    log.info("======== 타입 또는 ID목록으로 채널 조회 테스트 시작 ========");

    Channel publicChannel1 = createTestChannel(ChannelType.PUBLIC, "public1");
    Channel publicChannel2 = createTestChannel(ChannelType.PUBLIC, "public2");
    Channel privateChannel = createTestChannel(ChannelType.PRIVATE, "private");

    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of(privateChannel.getId()));

    assertThat(channels).hasSize(3);
    assertThat(channels).extracting("name").contains("public1", "public2", "private");
    assertThat(channels).extracting("type")
        .contains(ChannelType.PUBLIC, ChannelType.PUBLIC, ChannelType.PRIVATE);

    log.info("======== 타입 또는 ID목록으로 채널 조회 테스트 종료 ========");
  }

  @Test
  @Order(4)
  @DisplayName("조건에 맞는 채널이 없는 경우 빈 리스트를 반환한다")
  void findAllByTypeOrIdIn_ReturnsEmpty() {
    log.info("======== 타입 또는 ID목록으로 채널 조회 실패 테스트 시작 ========");

    createTestChannel(ChannelType.PRIVATE, "private1");
    createTestChannel(ChannelType.PRIVATE, "private2");
    createTestChannel(ChannelType.PUBLIC, "public1");
    createTestChannel(ChannelType.PRIVATE, "private3");

    List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

    assertThat(channels).isNotEmpty();

    log.info("======== 타입 또는 ID목록으로 채널 조회 실패 테스트 종료 ========");
  }

  @Test
  @Order(5)
  @DisplayName("채널을 삭제하면 더 이상 조회되지 않는다")
  void deleteChannelById() {
    log.info("======== 채널 삭제 테스트 시작 ========");

    Channel channel = createTestChannel(ChannelType.PUBLIC, "delete-channel");
    Optional<Channel> beforeDelete = channelRepository.findById(channel.getId());
    assertThat(beforeDelete).isPresent();

    channelRepository.deleteById(channel.getId());
    entityManager.flush();
    entityManager.clear();

    Optional<Channel> afterDelete = channelRepository.findById(channel.getId());
    assertThat(afterDelete).isEmpty();

    log.info("======== 채널 삭제 테스트 종료 ========");
  }
}
