import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.*;


public class FileMessageServiceTest {

  ChannelService channelService = FileChannelService.getInstance();
  UserService userService = FileUserService.getInstance();
  MessageServiceV2<ChatChannel> messageService = FileMessageService.getInstance(userService);

  @BeforeEach
  void setUp() {
    File file = new File(MESSAGE_FILE);
    File file2 = new File(USER_FILE);
    File file3 = new File(CHANNEL_FILE);

    if (file.exists()) {
      file.delete();
    }
    if(file2.exists()){
      file2.delete();
    }
    if(file3.exists()){
      file3.delete();
    }
  }

  @Test
  void saveAndReadMessage(){
    ChatChannel cc = new ChatChannel.ChatChannelBuilder("chat")
        .serverUUID("exampleServerUUID3")
        .categoryUUID("exampleCategoryUUID3")
        .build();
    channelService.createChannel(cc);


    User user1 = null;

    try {
      user1 = userService.createUser(
          new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
              .phoneNumber("01012345678")
              .nickname("asdfff")
              .description("testDiscription")
              .build());
    } catch (UserValidationException e) {
      throw new RuntimeException(e);
    }

    Message m = null;
    try {
      m = messageService.createMessage(user1.getUUID(), new Message.MessageBuilder(user1.getUUID(), cc.getUUID(), "testContent").build(), cc);
    } catch (MessageValidationException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertThat(m).isEqualTo(messageService.getMessageById(m.getUUID(), cc).get());
  }

  @Test
  void updateMessage(){
    ChatChannel cc = new ChatChannel.ChatChannelBuilder("chat2")
        .serverUUID("exampleServerUUID3")
        .categoryUUID("exampleCategoryUUID3")
        .build();
    channelService.createChannel(cc);


    User user1 = null;

    try {
      user1 = userService.createUser(
          new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
              .phoneNumber("01012345678")
              .nickname("asdfff")
              .description("testDiscription")
              .build());
    } catch (UserValidationException e) {
      throw new RuntimeException(e);
    }

    Message m = null;
    try {
      m = messageService.createMessage(user1.getUUID(), new Message.MessageBuilder(user1.getUUID(), cc.getUUID(), "testContent").build(), cc);
    } catch (MessageValidationException e) {
      throw new RuntimeException(e);
    }

    MessageUpdateDto updateDto = new MessageUpdateDto(Optional.of("UPDATED"), Optional.empty());
    messageService.updateMessage(cc, m.getUUID(), updateDto);

    Message m2 = messageService.getMessageById(m.getUUID(), cc).get();

    Assertions.assertThat(m2.getContent()).isEqualTo("UPDATED");
    Assertions.assertThat(m).isEqualTo(m2);
  }

  @Test
  void getMessagesByChannel(){

    ChatChannel cc = new ChatChannel.ChatChannelBuilder("chat")
        .serverUUID("exampleServerUUID3")
        .categoryUUID("exampleCategoryUUID3")
        .build();

    ChatChannel cc2 = new ChatChannel.ChatChannelBuilder("chat2")
        .serverUUID("exampleServerUUID55")
        .categoryUUID("exampleCategoryUUID55")
        .build();
    channelService.createChannel(cc2);
    channelService.createChannel(cc);


    User user1 = null;

    try {
      user1 = userService.createUser(
          new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
              .phoneNumber("01012345678")
              .nickname("asdfff")
              .description("testDiscription")
              .build());
    } catch (UserValidationException e) {
      throw new RuntimeException(e);
    }

    Message m = null;
    Message m2 = null;
    Message m3 = null;
    try {
      m = messageService.createMessage(user1.getUUID(), new Message.MessageBuilder(user1.getUUID(), cc.getUUID(), "testContent").build(), cc);
      m2 = messageService.createMessage(user1.getUUID(), new Message.MessageBuilder(user1.getUUID(), cc.getUUID(), "testContent1").build(), cc2);
      m3 = messageService.createMessage(user1.getUUID(), new Message.MessageBuilder(user1.getUUID(), cc.getUUID(), "testContent2").build(), cc2);
    } catch (MessageValidationException e) {
      throw new RuntimeException(e);
    }

    List<Message> messages = messageService.getMessagesByChannel(cc);
    List<Message> messages2 = messageService.getMessagesByChannel(cc2);

    Assertions.assertThat(messages.size()).isEqualTo(1);
    Assertions.assertThat(messages2.size()).isEqualTo(2);
  }
}
