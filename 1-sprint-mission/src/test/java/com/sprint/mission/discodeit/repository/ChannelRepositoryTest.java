package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@DataJpaTest
@Transactional
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private EntityManager entityManager;

  @Test
  void saveAndFindChannelSuccess() {
    // given
    Channel channel = new Channel(ChannelType.PUBLIC, "channel", "1channel");

    channelRepository.save(channel);

    entityManager.flush();
    entityManager.clear();

    // when
    Optional<Channel> foundChannel = channelRepository.findById(channel.id);

    // then
    assertThat(foundChannel).isPresent();
    assertThat(foundChannel.get().getName()).isEqualTo("channel");
  }


  @Test
  void findByIdFailWhenChannelDoesNotExist() {
    // when
    Optional<Channel> foundChannel = channelRepository.findById(UUID.randomUUID());

    // then
    assertThat(foundChannel).isEmpty();
  }
}
