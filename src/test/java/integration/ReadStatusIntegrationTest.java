package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class ReadStatusIntegrationTest {

  @Autowired
  private ReadStatusService readStatusService;

  @Autowired
  private UserService userService;

  @Autowired
  private ChannelService channelService;

  @Autowired private UserRepository userRepository;
  @Autowired private ChannelRepository channelRepository;
  @Autowired private MessageRepository messageRepository;
  @Autowired private BinaryContentRepository binaryContentRepository;
  @Autowired private ReadStatusRepository readStatusRepository;
  private UserResponseDto user;
  private PrivateChannelResponseDto channel;

  @BeforeEach
  void setUp(){

    userRepository.clear();
    channelRepository.clear();
    messageRepository.clear();
    binaryContentRepository.clear();
    readStatusRepository.clear();
    MockMultipartFile mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "fake image".getBytes());

    user = userService.saveUser(
        new CreateUserRequest(
            "username",
            "pwd",
            "test@example.com",
            "testNickname",
            "01012341234",
            mockFile,
            "description"
        )
    );

    channel = channelService.createPrivateChannel(
        new CreatePrivateChannelDto(
            Channel.ChannelType.VOICE, "serverId", 10, List.of(user.userId())
        )
    );
  }

  @Test
  void ReadStatus_생성_성공(){
    UserResponseDto tmpUser = userService.saveUser(
        new CreateUserRequest(
            "uname",
            "pwd2",
            "email2@email.com",
            "nick2",
            "01012312312",
            null,

            "description"
        )
    );

    CreateReadStatusDto dto = new CreateReadStatusDto(channel.channelId(), tmpUser.userId());
    ReadStatus readStatus = readStatusService.create(dto, false);

    assertThat(readStatus).isNotNull();
    assertThat(readStatus.getChannelId()).isEqualTo(channel.channelId());
    assertThat(readStatus.getUserId()).isEqualTo(tmpUser.userId());
  }

  @Test
  void 중복된_ReadStatus_생성_실패(){
    CreateReadStatusDto dto = new CreateReadStatusDto(channel.channelId(), user.userId());

    assertThatThrownBy(() -> readStatusService.create(dto, false)).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void ReadStatus_조회_성공(){
    UserResponseDto tmpUser = userService.saveUser(
        new CreateUserRequest(
            "uname",
            "pwd2",
            "email2@email.com",
            "nick2",
            "01012312312",
            null,

            "description"
        )
    );

    CreateReadStatusDto dto = new CreateReadStatusDto(channel.channelId(), tmpUser.userId());
    ReadStatus readStatus = readStatusService.create(dto, false);

    ReadStatus found = readStatusService.find(readStatus.getUUID());

    assertThat(found).isNotNull();
    assertThat(found).isEqualTo(readStatus);
  }

  @Test
  void 존재하지_않는_ReadStatus_조회_실패(){
    assertThatThrownBy(() -> readStatusService.find("invalid")).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void 사용자_ID_로_조회_성공(){
    List<ReadStatus> status = readStatusService.findAllByUserId(user.userId());

    assertThat(status).isNotEmpty();
    assertThat(status.get(0).getUserId()).isEqualTo(user.userId());
    assertThat(status.get(0).getChannelId()).isEqualTo(channel.channelId());
  }

  @Test
  void 채널_ID_로_조회_성공(){
    List<ReadStatus> status = readStatusService.findAllByChannelId(channel.channelId());

    assertThat(status).isNotEmpty();
    assertThat(status.get(0).getUserId()).isEqualTo(user.userId());
    assertThat(status.get(0).getChannelId()).isEqualTo(channel.channelId());
  }

  @Test
  void ReadStatus_업데이트_성공() throws InterruptedException {
    List<ReadStatus> statuses = readStatusService.findAllByUserId(user.userId());
    ReadStatus status = statuses.get(0);
    Instant timeBefore = status.getLastReadAt();

    Thread.sleep(10);
    CreateReadStatusDto dto = new CreateReadStatusDto(channel.channelId(), user.userId());

    ReadStatus updated = readStatusService.update(dto);

    assertThat(updated.getLastReadAt()).isAfter(timeBefore);
  }

  @Test
  void 채널별_삭제_성공(){
    readStatusService.deleteByChannel(channel.channelId());

    assertThat(readStatusService.findAllByChannelId(channel.channelId())).isEmpty();
  }

  @Test
  void 존재하지_않는_사용자_ReadStatus_업데이트_실패() {
    CreateReadStatusDto dto = new CreateReadStatusDto(channel.channelId(), "invalid-user");

    assertThatThrownBy(() -> readStatusService.update(dto))
        .isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void 존재하지_않는_채널_ReadStatus_업데이트_실패() {
    CreateReadStatusDto dto = new CreateReadStatusDto("invalid-channel", user.userId());

    assertThatThrownBy(() -> readStatusService.update(dto))
        .isInstanceOf(InvalidOperationException.class);
  }


}
