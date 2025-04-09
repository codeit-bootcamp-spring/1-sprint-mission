package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.JpaConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.p6spy.P6spyConfig;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({JpaConfig.class, P6spyConfig.class})
@ActiveProfiles("test")
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("findAllByType - PUBLIC 타입 채널만 조회한다")
  void findAllByType_success() {
    // given
    persistChannel("c1", ChannelType.PUBLIC);
    persistChannel("c2", ChannelType.PUBLIC);
    persistChannel("c3", ChannelType.PRIVATE);
    em.flush();
    em.clear();
    // when
    List<Channel> channels = channelRepository.findAllByType(ChannelType.PUBLIC);

    //then
    assertThat(channels).hasSize(2);
    assertThat(channels).allMatch(c -> c.getType() == ChannelType.PUBLIC);
  }

  @Test
  @DisplayName("findAllByType - 해당 타입 체널이 없으면 빈 리스트")
  void findAllByType_shouldReturnEmptyList_whenNoSuchTypeIsFound() {
    persistChannel("private1", ChannelType.PRIVATE);
    em.flush();
    em.clear();

    List<Channel> result = channelRepository.findAllByType(ChannelType.PUBLIC);
    assertThat(result).isEmpty();
  }


  @Test
  @DisplayName("findByIdInOrType - ID 혹은 Type 이 맞다면 채널 반환")
  void findByIdInOrType_success() {
    // given
    Channel c1 = persistChannel("c1", ChannelType.PUBLIC);
    Channel c2 = persistChannel("c2", ChannelType.PUBLIC);
    Channel c3 = persistChannel("c3", ChannelType.PRIVATE);

    // when
    List<Channel> result = channelRepository.findByIdInOrType(List.of(c1.getId()),
        ChannelType.PRIVATE);

    // then
    assertThat(result).hasSize(2);
    assertThat(result).containsExactlyInAnyOrder(c1, c3);
  }

  @Test
  @DisplayName("findByIdInOrType - 일치하는 조건이 없다면 빈 리스트 반환")
  void findByIdInOrType_fail() {
    persistChannel("c1", ChannelType.PRIVATE);

    // when
    List<Channel> result = channelRepository.findByIdInOrType(List.of(UUID.randomUUID()),
        ChannelType.PUBLIC);

    // then
    assertThat(result).isEmpty();
  }

  private Channel persistChannel(String name, ChannelType type) {
    Channel c = new Channel(type, name, "tmpDiscription", Collections.emptyList());
    return em.persist(c);
  }
}
