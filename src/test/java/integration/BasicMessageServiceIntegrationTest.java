package integration;

import com.sprint.mission.DiscodeitApplication;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.util.FileType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = DiscodeitApplication.class)
public class BasicMessageServiceIntegrationTest {

  @Autowired
  private MessageService messageService;
  @Autowired
  private UserService userService;
  @Autowired
  private ChannelService channelService;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;
  private User user;
  private PrivateChannelResponseDto channel, channel2;

  @BeforeEach
  void setUp(){

    userRepository.clear();
    channelRepository.clear();;
    messageRepository.clear();
    binaryContentRepository.clear();

    user = userService.createUser(
        new CreateUserDto(
            "username",
            "pwd",
            "test@example.com",
            "testNickname",
            "01012341234",
            new byte[]{1},
            "profileImage",
            "jpg",
            "description"
        )
    );

    channel = channelService.createPrivateChannel(
        new CreatePrivateChannelDto(
            Channel.ChannelType.VOICE, "serverId", 10, List.of(user.getUUID())
        )
    );

    channel2 = channelService.createPrivateChannel(
        new CreatePrivateChannelDto(
            Channel.ChannelType.VOICE, "serverId", 10, List.of(user.getUUID())
        )
    );

  }

  @Test
  void 메시지_생성_성공(){
    CreateMessageDto messageDto = new CreateMessageDto(user.getUUID(), channel.channelId(), "message", List.of());

    MessageResponseDto res = messageService.createMessage(messageDto);

    assertThat(res).isNotNull();
    assertThat(res.data()).isEmpty();
    assertThat(res.userId()).isEqualTo(user.getUUID());
    assertThat(res.content()).isEqualTo("message");
  }

  @Test
  void 존재하지_않는_사용자_메시지_생성_실패(){
    CreateMessageDto messageDto = new CreateMessageDto("invalid-user", channel.channelId(), "message", List.of());

    assertThatThrownBy(() -> messageService.createMessage(messageDto)).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 존재하지_않는_채널_메시지_생성_실패(){
    CreateMessageDto messageDto = new CreateMessageDto(user.getUUID(), "invalid-channel", "message", List.of());

    assertThatThrownBy(() -> messageService.createMessage(messageDto)).isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  void 메시지_ID_조회_성공(){
    CreateMessageDto messageDto = new CreateMessageDto(user.getUUID(), channel.channelId(), "message", List.of());

    MessageResponseDto res = messageService.createMessage(messageDto);
    MessageResponseDto message = messageService.getMessageById(res.messageId());

    assertThat(message).isNotNull();
    assertThat(message.content()).isEqualTo("message");
  }

  @Test
  void 존재하지_않는_메시지ID(){
    assertThatThrownBy(()-> messageService.getMessageById("invalid")).isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void 채널_ID_로_메시지_조회(){
    CreateMessageDto messageDto = new CreateMessageDto(user.getUUID(), channel.channelId(), "message", List.of());
    CreateMessageDto messageDto2 = new CreateMessageDto(user.getUUID(), channel.channelId(), "message", List.of());
    CreateMessageDto messageDto3 = new CreateMessageDto(user.getUUID(), channel2.channelId(), "channel2Message", List.of());


    messageService.createMessage(messageDto);
    messageService.createMessage(messageDto2);
    messageService.createMessage(messageDto3);

    List<MessageResponseDto> responseDtos = messageService.getMessagesByChannel(channel.channelId());

    assertThat(responseDtos).isNotEmpty();
    assertThat(responseDtos).hasSize(2);
    assertThat(responseDtos).extracting("content").containsOnly("message");
  }

  @Test
  void 메시지_수정_성공(){
    CreateMessageDto messageDto = new CreateMessageDto(user.getUUID(), channel.channelId(), "message", List.of());
    MessageResponseDto created = messageService.createMessage(messageDto);

    MessageUpdateDto updateDto = new MessageUpdateDto(created.messageId(), user.getUUID(), "new message", List.of());
    MessageResponseDto updatedRes = messageService.updateMessage(updateDto);

    MessageResponseDto m = messageService.getMessageById(created.messageId());

    assertThat(m).isNotNull();
    assertThat(m.content()).isEqualTo("new message");
  }

  @Test
  void 존재하지_않는_메시지_수정_실패(){
    MessageUpdateDto updateDto = new MessageUpdateDto("invalid", "invalid", "message", List.of());

    assertThatThrownBy(() -> messageService.updateMessage(updateDto)).isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void 메시지_삭제_성공(){
    CreateMessageDto messageDto = new CreateMessageDto(user.getUUID(), channel.channelId(), "message", List.of());
    MessageResponseDto created = messageService.createMessage(messageDto);

    messageService.deleteMessage(created.messageId());

    assertThatThrownBy(() -> messageService.getMessageById(created.messageId())).isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  void 메시지의_첨부파일_불러오기_성공(){
    BinaryContentDto binaryContentDto = new BinaryContentDto("image1", FileType.JPG, 3, new byte[]{1,2,3});
    BinaryContentDto binaryContentDto2 = new BinaryContentDto("image2", FileType.JPG, 4, new byte[]{1,2,3,4});
    CreateMessageDto messageDto = new CreateMessageDto(user.getUUID(), channel.channelId(), "message", List.of(binaryContentDto, binaryContentDto2));
    MessageResponseDto created = messageService.createMessage(messageDto);

    assertThat(created.data()).extracting("fileName").contains("image1", "image2");
  }

  @Test
  void 다른_사용자의_메시지_수정_실패(){
    User tmpUser = userService.createUser(
        new CreateUserDto(
            "uname",
            "pwd2",
            "email2@email.com",
            "nick2",
            "01012312312",
            null,
            null,
            null,
            "description"
        )
    );

    CreateMessageDto messageDto = new CreateMessageDto(user.getUUID(), channel.channelId(), "message", List.of());
    MessageResponseDto created = messageService.createMessage(messageDto);

    MessageUpdateDto updateDto = new MessageUpdateDto(created.messageId(), tmpUser.getUUID(), "new content", List.of());

    assertThatThrownBy(() -> messageService.updateMessage(updateDto)).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void 채널에_속하지_않은_사용자의_메시지_생성_실패(){
    User tmpUser = userService.createUser(
        new CreateUserDto(
            "uname",
            "pwd2",
            "email2@email.com",
            "nick2",
            "01012312312",
            null,
            null,
            null,
            "description"
        )
    );

    CreateMessageDto messageDto = new CreateMessageDto(tmpUser.getUUID(), channel.channelId(), "content", List.of());

    assertThatThrownBy(() -> messageService.createMessage(messageDto)).isInstanceOf(InvalidOperationException.class );
  }
}
