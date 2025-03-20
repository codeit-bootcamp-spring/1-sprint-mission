package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Rollback(false)
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BasicChannelServiceTest {

  @Autowired
  ChannelService channelService;

  @Autowired
  ChannelRepository channelRepository;

  @Autowired
  ReadStatusRepository readStatusRepository;

  @Autowired
  UserService userService;

  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("공개 채널 생성")
  void publicChannel() {
    UUID userId = userService.findAll().get(0).id();
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("ch1",
        "안녕");

    ChannelDto createChannel = channelService.createPublic(request);

    List<ChannelDto> channels = channelService.findAllChannelsByUserId(userId);

    ChannelDto findChannel = channels.get(0);

    assertThat(findChannel.type()).isEqualTo(ChannelType.PUBLIC);
    assertThat(findChannel.name()).isEqualTo(createChannel.name());
    assertThat(findChannel.description()).isEqualTo(createChannel.description());
    assertThat(findChannel.participants()).isEqualTo(createChannel.participants());
  }

  @Test
  @DisplayName("비공개 채널 생성")
  void privateChannel() {
    List<UUID> list = userService.findAll().stream()
        .map(UserCreateResponse::id)
        .toList();

    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(list);
    ChannelDto channelDto = channelService.createPrivate(request);

    List<UserDto> participants = channelDto.participants();
    List<UUID> uuids = participants.stream()
        .map(UserDto::id)
        .toList();

    assertThat(uuids).isEqualTo(list);
  }

  @Test
  @DisplayName("채널 삭제")
  void delete() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("ch1",
        "안녕");
    ChannelDto createChannel = channelService.createPublic(request);

    em.flush();
    em.clear();

    channelService.remove(createChannel.id());

    List<ReadStatus> byChannelId = readStatusRepository.findByChannel_Id(createChannel.id());
    assertThat(byChannelId.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("채널 정보 수정")
  void update() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("ch1",
        "안녕");
    ChannelDto createChannel = channelService.createPublic(request);

    channelService.update(createChannel.id(),
        new ChannelModifyRequest("newName", "newDescription"));

    Channel channel = channelRepository.findById(createChannel.id()).get();

    assertThat(channel.getName()).isEqualTo("newName");
    assertThat(channel.getDescription()).isEqualTo("newDescription");
  }

  @Test
  @DisplayName("유저가 참여 중인 채널 목록 조회")
  void findByUserId() {
    UserCreateResponse user = userService.findAll().get(0);
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("ch1",
        "안녕");
    ChannelDto createChannel = channelService.createPublic(request);

    List<ChannelDto> channelDtos = channelService.findAllChannelsByUserId(user.id());
    
  }


  @BeforeAll
  void setupTestData() throws IOException {
    for (int i = 1; i <= 3; i++) {
      String username = "user" + i;
      String email = "user" + i + "@mail.com";

      UserCreateRequest request = UserCreateRequest.builder()
          .username(username)
          .email(email)
          .password("password123")
          .build();

      MockMultipartFile file = new MockMultipartFile(
          "file",
          "profile" + i + ".txt",
          "text/plain",
          ("dummy content for user " + i).getBytes()
      );

      userService.join(request, file);
    }
  }
}