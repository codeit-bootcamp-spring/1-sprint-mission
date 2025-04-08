package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("유저가 속한 채널 모두 찾기 - 성공")
  void findAllByTypeOrIdInSuccess() {
    Channel channel1 = new Channel(ChannelType.PUBLIC, "공지 채널", "공지 채널입니다.");
    Channel channel2 = new Channel(ChannelType.PUBLIC, "일반 채널", null);
    Channel channel3 = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.saveAll(List.of(channel1, channel2, channel3));

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of(channel3.getId()));

    assertEquals(3, result.size());
    List<String> names = result.stream().map(Channel::getName).toList();
    assertTrue(names.contains("공지 채널"));
  }

  @Test
  @DisplayName("유저가 속한 채널 모두 찾기 - 실패 (일치 항목 없음)")
  void findAllByTypeOrIdInFail() {
    Channel channel1 = new Channel(ChannelType.PUBLIC, "공지 채널", "공지 채널입니다.");
    Channel channel2 = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.saveAll(List.of(channel1, channel2));

    UUID nonExistentId = UUID.randomUUID();

    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of(nonExistentId));

    assertEquals(1, result.size());
  }
}
