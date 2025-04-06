package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  private Channel createChannel(ChannelType type, String name, String desc) {
    return channelRepository.save(new Channel(type, name, desc));
  }

  @Test
  void testFindAllByTypeOrIdIn_successByType() {
    // given
    Channel publicChannel = new Channel(ChannelType.PUBLIC, "Public Name", "공개 채널이야!");
    channelRepository.save(publicChannel);

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getType()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  void testFindAllByTypeOrIdIn_failWhenNoMatch() {
    // given
    // 아무 채널도 저장하지 않음!

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PRIVATE, List.of());

    // then
    assertThat(result).isEmpty(); // 아무것도 없어야 해!
  }
}
