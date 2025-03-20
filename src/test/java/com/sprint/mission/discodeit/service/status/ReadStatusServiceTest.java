package com.sprint.mission.discodeit.service.status;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Rollback(false)
@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadStatusServiceTest {

  @Autowired
  UserService userService;

  @Autowired
  ReadStatusService readStatusService;

  @Autowired
  ChannelService channelService;

  @Autowired
  private PlatformTransactionManager transactionManager;

  PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
      "public channel1", "공개 채널");
  ChannelDto channelDto = null;


  @Test
  @DisplayName("읽음 상태 생성")
  void create() {
    UserCreateResponse user = userService.findAll().get(0);

    ReadStatusDto readStatusDto = readStatusService.create(
        new ReadStatusRequest(user.id(), channelDto.id(), Instant.now()));

  }

  @Test
  @DisplayName("읽음 상태 수정")
  void update() throws InterruptedException {
    UserCreateResponse user = userService.findAll().get(0);
    ReadStatusDto readStatusDto = readStatusService.findByUserId(user.id()).get(0);
    Instant present = readStatusDto.lastReadAt();

    Thread.sleep(1000);

    ReadStatusDto update = readStatusService.update(readStatusDto.id(),
        new ReadStatusUpdateRequest(Instant.now()));

    assertThat(Duration.between(present, update.lastReadAt()).getSeconds())
        .isGreaterThanOrEqualTo(1L);
  }

  @Test
  @DisplayName("읽음 상태 조회")
  void readAll() {
    UserCreateResponse user = userService.findAll().get(0);

    List<ReadStatusDto> readStatus = readStatusService.findByUserId(user.id());

    assertThat(readStatus.get(0).userId()).isEqualTo(user.id());
  }


  @BeforeAll
  void setupTestData() {
    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
    transactionTemplate.execute(status -> {
      for (int i = 1; i <= 2; i++) {
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

        try {
          userService.join(request, file);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      channelDto = channelService.createPublic(publicChannelCreateRequest);

      return null;
    });
  }
}