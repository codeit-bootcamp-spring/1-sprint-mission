package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sprint.mission.discodeit.config.QuerydslConfig;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.logging.LogConfiguration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import({LogConfiguration.class, QuerydslConfig.class})
class ReadStatusRepositoryTest {

  @Autowired
  ReadStatusRepository readStatusRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ChannelRepository channelRepository;

  @Test
  @DisplayName("유저가 참여 중인 모든 채널 가져오기")
  void findAllChannelsInUser() throws Exception {
    User user2 = userRepository.findUserByUsername("user2");

    List<ReadStatus> allChannelsInUser = readStatusRepository.findAllChannelsInUser(user2.getId());

    assertNotNull(allChannelsInUser);
    assertEquals(2, allChannelsInUser.size());
  }

  @Test
  @DisplayName("유저가 참여 중인 모든 채널 가져오기 - 실패")
  void findAllChannelsInUserFail() throws Exception {
    // 없는 유저 가져오기
    User user = userRepository.findUserByUsername("user99");

    assertThrows(NullPointerException.class,
        () -> readStatusRepository.findAllChannelsInUser(user.getId()));

  }

  @Test
  @DisplayName("채널의 모든 read status 가져오기")
  void findAllByChannel() throws Exception {
    Channel channel = channelRepository.findById(
            UUID.fromString("6f17a8d1-77d7-437d-811e-d98db3bd30bc"))
        .orElse(null);

    List<ReadStatus> readStatusList = readStatusRepository.findAllByChannel(channel);

    assertNotNull(readStatusList);
    assertEquals(2, readStatusList.size());
  }
}