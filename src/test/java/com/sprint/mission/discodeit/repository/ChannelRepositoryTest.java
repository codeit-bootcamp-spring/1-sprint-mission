package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ChannelRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ChannelRepository channelRepository;

  private Channel testPublicChannel;
  private Channel testPrivateChannel;

  @BeforeEach
  void setUp() {
    // 테스트 데이터 생성
    testPublicChannel = Channel.builder()
        .name("테스트 공개 채널")
        .description("테스트 설명")
        .type(ChannelType.PUBLIC)
        .build();

    testPrivateChannel = Channel.builder()
        .name("테스트 비공개 채널")
        .description("테스트 설명")
        .type(ChannelType.PRIVATE)
        .build();

    entityManager.flush();
  }

  @Test
  @DisplayName("공개 채널 저장 테스트")
  void save_PublicChannel() {
    // When
    Channel savedChannel = channelRepository.save(testPublicChannel);
    entityManager.flush();
    entityManager.clear();

    // Then
    Channel foundChannel = entityManager.find(Channel.class, savedChannel.getId());
    assertThat(foundChannel).isNotNull();
    assertThat(foundChannel.getName()).isEqualTo("테스트 공개 채널");
    assertThat(foundChannel.getDescription()).isEqualTo("테스트 설명");
    assertThat(foundChannel.getType()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  @DisplayName("비공개 채널 저장 테스트")
  void save_PrivateChannel() {
    // When
    Channel savedChannel = channelRepository.save(testPrivateChannel);
    entityManager.flush();
    entityManager.clear();

    // Then
    Channel foundChannel = entityManager.find(Channel.class, savedChannel.getId());
    assertThat(foundChannel).isNotNull();
    assertThat(foundChannel.getName()).isEqualTo("테스트 비공개 채널");
    assertThat(foundChannel.getDescription()).isEqualTo("테스트 설명");
    assertThat(foundChannel.getType()).isEqualTo(ChannelType.PRIVATE);
  }

  @Test
  @DisplayName("ID로 채널 조회 테스트")
  void findById_Success() {
    // Given
    Channel savedChannel = entityManager.persistAndFlush(testPublicChannel);
    entityManager.clear();

    // When
    Optional<Channel> foundChannel = channelRepository.findById(savedChannel.getId());

    // Then
    assertThat(foundChannel).isPresent();
    assertThat(foundChannel.get().getName()).isEqualTo("테스트 공개 채널");
    assertThat(foundChannel.get().getType()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  @DisplayName("모든 채널 조회 테스트")
  void findAll_Success() {
    // Given
    entityManager.persistAndFlush(testPublicChannel);
    entityManager.persistAndFlush(testPrivateChannel);
    entityManager.clear();

    // When
    List<Channel> channels = channelRepository.findAll();

    // Then
    assertThat(channels).isNotEmpty();
    assertThat(channels.size()).isGreaterThanOrEqualTo(2);
    assertThat(channels).anyMatch(c -> c.getName().equals("테스트 공개 채널"));
    assertThat(channels).anyMatch(c -> c.getName().equals("테스트 비공개 채널"));
  }

  @Test
  @DisplayName("채널 삭제 테스트")
  void deleteById_Success() {
    // Given
    Channel savedChannel = entityManager.persistAndFlush(testPublicChannel);
    entityManager.clear();

    // When
    channelRepository.deleteById(savedChannel.getId());
    entityManager.flush();
    entityManager.clear();

    // Then
    Channel deletedChannel = entityManager.find(Channel.class, savedChannel.getId());
    assertThat(deletedChannel).isNull();
  }

  @Test
  @DisplayName("채널 업데이트 테스트")
  void update_Success() {
    // Given
    Channel savedChannel = entityManager.persistAndFlush(testPublicChannel);

    // When
    savedChannel.setName("수정된 채널명");
    savedChannel.setDescription("수정된 설명");
    channelRepository.save(savedChannel);
    entityManager.flush();
    entityManager.clear();

    // Then
    Channel updatedChannel = entityManager.find(Channel.class, savedChannel.getId());
    assertThat(updatedChannel).isNotNull();
    assertThat(updatedChannel.getName()).isEqualTo("수정된 채널명");
    assertThat(updatedChannel.getDescription()).isEqualTo("수정된 설명");
  }

  @Test
  @DisplayName("채널 타입으로 채널 찾기 테스트")
  void findByType_Success() {
    // Given
    entityManager.persistAndFlush(testPublicChannel);
    entityManager.persistAndFlush(testPrivateChannel);
    entityManager.clear();

    // When
    List<Channel> publicChannels = channelRepository.findByType(ChannelType.PUBLIC);
    List<Channel> privateChannels = channelRepository.findByType(ChannelType.PRIVATE);

    // Then
    assertThat(publicChannels).isNotEmpty();
    assertThat(privateChannels).isNotEmpty();
    assertThat(publicChannels).allMatch(c -> c.getType() == ChannelType.PUBLIC);
    assertThat(privateChannels).allMatch(c -> c.getType() == ChannelType.PRIVATE);
  }

  @Test
  @DisplayName("채널 이름으로 채널 찾기 테스트")
  void findByNameContaining_Success() {
    // Given
    String channelName = "특별한 채널 이름";
    Channel channel = Channel.builder()
        .name(channelName)
        .description("특별한 설명")
        .type(ChannelType.PUBLIC)
        .build();
    entityManager.persistAndFlush(channel);
    entityManager.clear();

    // When
    List<Channel> foundChannels = channelRepository.findByNameContaining(channelName);

    // Then
    assertThat(foundChannels).isNotEmpty();
    assertThat(foundChannels).anyMatch(c -> c.getName().equals(channelName));
  }
} 