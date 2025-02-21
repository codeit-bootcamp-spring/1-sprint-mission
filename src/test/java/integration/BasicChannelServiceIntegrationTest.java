package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static com.sprint.mission.discodeit.constant.FileConstant.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class BasicChannelServiceIntegrationTest {

  @Autowired
  private ChannelService channelService;
  @Autowired
  private UserService userService;
  @Autowired
  private ReadStatusService readStatusService;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ReadStatusRepository readStatusRepository;
  private CreateUserDto userDto, userDto2;
  private User user, user2;

  @BeforeEach
  void setUp(){
    channelRepository.clear();
    messageRepository.clear();
    userRepository.clear();
    readStatusRepository.clear();


    userDto = new CreateUserDto(
        "testUser", "password123", "test@example.com",
        "testNickname", "01012345678", new byte[]{1},
        "profileImage", "jpg", "Test Description"
    );

    userDto2 = new CreateUserDto(
        "testUser1", "password2", "test2@example.com",
        "testNickname2", "01012345672", new byte[]{1},
        "profileImage", "jpg", "Test Description"
    );

    user = userService.createUser(userDto);
    user2 = userService.createUser(userDto2);
  }

  @Test
  void 비공개_채널_생성_성공(){
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        25,
        List.of(user.getUUID())
    );

    PrivateChannelResponseDto res = channelService.createPrivateChannel(dto);

    assertThat(res).isNotNull();
    assertThat(res.serverId()).isEqualTo("serverId");
  }

  @Test
  void 공개_채널_생성_성공(){
    CreateChannelDto dto = new CreateChannelDto(
        "channelName",
        Channel.ChannelType.VOICE,
        "serverId",
        25
    );

    PublicChannelResponseDto res = channelService.createPublicChannel(dto);

    assertThat(res).isNotNull();
    assertThat(res.channelName()).isEqualTo("channelName");
    assertThat(res.serverId()).isEqualTo("serverId");
  }

  @Test
  void 존재하지_않는_유저_채널_생성_실패(){
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        25,
        List.of(user.getUUID(), "randomUUID")
    );

    assertThatThrownBy(() -> channelService.createPrivateChannel(dto)).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 비공개_채널_생성시_읽기상태_자동_생성(){
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        25,
        List.of(user.getUUID())
    );

    PrivateChannelResponseDto res = channelService.createPrivateChannel(dto);

    List<ReadStatus> statuses = readStatusService.findAllByChannelId(res.channelId());
    assertThat(statuses).isNotNull();
    assertThat(statuses).hasSize(1);
    assertThat(statuses.get(0).getUserId()).isEqualTo(user.getUUID());
  }


  @Test
  void 존재하지_않는_채널_조회_실패(){
    assertThatThrownBy(() -> channelService.getChannelById("wrongId")).isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  void 채널_조회_성공(){
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        25,
        List.of(user.getUUID())
    );

    PrivateChannelResponseDto res = channelService.createPrivateChannel(dto);

    assertThat(channelService.getChannelById(res.channelId())).isNotNull();
  }

  @Test
  void 유저_ID로_채널_조회_성공(){
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        44,
        List.of(user.getUUID())
    );


    CreateChannelDto dto2 = new CreateChannelDto(
        "channelName",
        Channel.ChannelType.VOICE,
        "serverId",
        25
    );

    CreatePrivateChannelDto dto3 = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        44,
        List.of()
    );

    channelService.createPrivateChannel(dto);
    channelService.createPublicChannel(dto2);
    channelService.createPrivateChannel(dto3);


    List<FindChannelResponseDto> res = channelService.findAllChannelsByUserId(user.getUUID());

    assertThat(res).isNotNull();
    assertThat(res).hasSize(2);

    res.stream().forEach(ch -> {
          if(ch.channelName() != null){
            assertThat(ch.channelName()).isEqualTo("channelName");
          }
        }
    );
  }

  @Test
  void 채널_정보_업데이트_성공(){
    CreateChannelDto dto = new CreateChannelDto(
        "channelName",
        Channel.ChannelType.VOICE,
        "serverId",
        25
    );

    PublicChannelResponseDto res = channelService.createPublicChannel(dto);

    ChannelUpdateDto updateDto = new ChannelUpdateDto(res.channelId(), "newChannelName", 10);

    channelService.updateChannel(updateDto);

    FindChannelResponseDto channelAfterUpdate = channelService.getChannelById(res.channelId());

    assertThat(channelAfterUpdate.channelName()).isEqualTo("newChannelName");
    assertThat(channelAfterUpdate.maxNumberOfPeople()).isEqualTo(10);
  }

  @Test
  void 비공개_채널_업데이트_실패(){
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        44,
        List.of(user.getUUID())
    );

    PrivateChannelResponseDto res = channelService.createPrivateChannel(dto);

    ChannelUpdateDto updateDto = new ChannelUpdateDto(res.channelId(), "new", 10);

    assertThatThrownBy(() -> channelService.updateChannel(updateDto)).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void 채널_삭제_성공(){
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        44,
        List.of(user.getUUID())
    );

    PrivateChannelResponseDto res = channelService.createPrivateChannel(dto);

    channelService.deleteChannel(res.channelId());

    assertThatThrownBy(()->channelService.getChannelById(res.channelId())).isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  void 채널_삭제_후_읽기상태_삭제(){
    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        44,
        List.of(user.getUUID())
    );

    PrivateChannelResponseDto res = channelService.createPrivateChannel(dto);
    assertThat(readStatusService.findAllByChannelId(res.channelId())).isNotEmpty();
    channelService.deleteChannel(res.channelId());

    assertThat(readStatusService.findAllByChannelId(res.channelId())).isEmpty();
  }

  @Test
  void 공개_채널은_모든_사용자에게_표시됨(){
    CreateChannelDto dto = new CreateChannelDto(
        "channelName",
        Channel.ChannelType.VOICE,
        "serverId",
        25
    );
    PublicChannelResponseDto res = channelService.createPublicChannel(dto);

    List<FindChannelResponseDto> user1Channels = channelService.findAllChannelsByUserId(user.getUUID());
    List<FindChannelResponseDto> user2Channels = channelService.findAllChannelsByUserId(user2.getUUID());

    assertThat(user1Channels).extracting("channelName").contains("channelName");
    assertThat(user2Channels).extracting("channelName").contains("channelName");
  }

  @Test
  void 비공개_채널이_초대된_사용자에게만_표시됨() {

    CreatePrivateChannelDto dto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "serverId",
        25,
        List.of(user.getUUID())
    );
    PrivateChannelResponseDto res = channelService.createPrivateChannel(dto);


    List<FindChannelResponseDto> channelsForUser1 = channelService.findAllChannelsByUserId(user.getUUID());
    List<FindChannelResponseDto> channelsForUser2 = channelService.findAllChannelsByUserId(user2.getUUID());


    assertThat(channelsForUser1).extracting("channelId").contains(res.channelId());
    assertThat(channelsForUser2).extracting("channelId").doesNotContain(res.channelId());
  }

}
