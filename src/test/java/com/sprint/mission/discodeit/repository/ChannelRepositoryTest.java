package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("채널 저장 후 ID로 조회 성공 테스트")
  void testSaveAndFindChannelSuccess() {
    // given
    Channel channel = new Channel(ChannelType.PUBLIC, "Test Channel", "Test Description");

    // when
    Channel saved = channelRepository.save(channel);
    Optional<Channel> found = channelRepository.findById(saved.getId());

    // then
    assertThat(saved.getId()).isNotNull();
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("Test Channel");
  }

  @Test
  @DisplayName("존재하지 않는 채널 ID 조회 실패 테스트")
  void testFindNonExistentChannelFail() {
    // given
    UUID randomId = UUID.randomUUID();

    // when
    Optional<Channel> found = channelRepository.findById(randomId);

    // then
    assertThat(found).isEmpty();
  }

  @Test
  @DisplayName("findAllByTypeOrIdIn 성공 테스트: 공개 채널 + privateChannel ID 지정")
  void testFindAllByTypeOrIdInSuccess() {
    // given
    Channel publicChannel = new Channel(ChannelType.PUBLIC, "Public Channel", "Desc");
    Channel privateChannel = new Channel(ChannelType.PRIVATE, "Private Channel", "Desc");
    channelRepository.save(publicChannel);
    channelRepository.save(privateChannel);

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(
        ChannelType.PUBLIC,
        List.of(privateChannel.getId())
    );

    // then
    assertThat(result).hasSize(2);
    assertThat(result).extracting("type").contains(ChannelType.PUBLIC, ChannelType.PRIVATE);
  }

  @Test
  @DisplayName("findAllByTypeOrIdIn 실패 테스트: 아무 것도 매칭되지 않는 경우")
  void testFindAllByTypeOrIdInFail_noMatch() {
    // given
    Channel privateChannel = new Channel(ChannelType.PRIVATE, "Private Channel", "Desc");
    channelRepository.save(privateChannel);

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(
        ChannelType.PUBLIC,
        Collections.emptyList()
    );

    // then
    assertThat(result).isEmpty();
  }
}
