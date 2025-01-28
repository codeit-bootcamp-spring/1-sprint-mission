package service;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class BasicMessageServiceTest {

  UserRepository userRepository = FileUserRepository.getInstance();
  ChannelRepository channelRepository = FileChannelRepository.getInstance();
  MessageRepository messageRepository = FileMessageRepository.getInstance();
  UserService userService = BasicUserService.getInstance(userRepository);
  ChannelService channelService = BasicChannelService.getInstance(channelRepository);
  MessageServiceV2<ChatChannel> messageService = BasicMessageService.getInstance(messageRepository, userRepository, channelRepository);

  @BeforeEach
  void beforeEach(){
    userRepository.clear();
    channelRepository.clear();
    messageRepository.clear();
  }

  @Test
  void createAndRead(){
    ChatChannel channel = new ChatChannel.ChatChannelBuilder("chatChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();

    User user = new User.UserBuilder("kiki1875", "1234", "example@gmail.com")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();

    userService.createUser(user);
    channelService.createChannel(channel);

    Message message = new Message.MessageBuilder(user.getUUID(), channel.getUUID(), "example").build();

    messageService.createMessage(user.getUUID(), message, channel);

    Assertions.assertThat(messageService.getMessageById(message.getUUID(), channel).get()).isEqualTo(message);
  }

  @Test
  void readByChannel(){
    ChatChannel channel = new ChatChannel.ChatChannelBuilder("chatChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();

    ChatChannel channel2 = new ChatChannel.ChatChannelBuilder("chatChannel2")
        .serverUUID("exampelUUID2")
        .categoryUUID("exampleUUID2")
        .build();

    User user = new User.UserBuilder("kiki1875", "1234", "example@gmail.com")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();

    userService.createUser(user);
    channelService.createChannel(channel);
    channelService.createChannel(channel2);

    Message message1 = new Message.MessageBuilder(user.getUUID(), channel.getUUID(), "example").build();
    Message message2 = new Message.MessageBuilder(user.getUUID(), channel.getUUID(), "example").build();
    Message message3 = new Message.MessageBuilder(user.getUUID(), channel2.getUUID(), "example").build();

    messageService.createMessage(user.getUUID(), message1, channel);
    messageService.createMessage(user.getUUID(), message2, channel);
    messageService.createMessage(user.getUUID(), message3, channel2);

    Assertions.assertThat(messageService.getMessagesByChannel(channel)).hasSize(2);
    Assertions.assertThat(messageService.getMessagesByChannel(channel2)).hasSize(1);
  }

  @Test
  void update(){
    ChatChannel channel = new ChatChannel.ChatChannelBuilder("chatChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();

    User user = new User.UserBuilder("kiki1875", "1234", "example@gmail.com")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();

    userService.createUser(user);
    channelService.createChannel(channel);

    Message message = new Message.MessageBuilder(user.getUUID(), channel.getUUID(), "example").build();

    messageService.createMessage(user.getUUID(), message, channel);

    MessageUpdateDto updateDto = new MessageUpdateDto(
        Optional.of("updated"), Optional.empty()
    );

    messageService.updateMessage(channel, message.getUUID(), updateDto);
    Assertions.assertThat(messageService.getMessageById(message.getUUID(), channel).get().getContent()).isEqualTo("updated");
  }

  @Test
  void delete(){
    ChatChannel channel = new ChatChannel.ChatChannelBuilder("chatChannel")
        .serverUUID("exampelUUID")
        .categoryUUID("exampleUUID")
        .build();

    User user = new User.UserBuilder("kiki1875", "1234", "example@gmail.com")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();

    userService.createUser(user);
    channelService.createChannel(channel);

    Message message = new Message.MessageBuilder(user.getUUID(), channel.getUUID(), "example").build();

    messageService.createMessage(user.getUUID(), message, channel);

    messageService.deleteMessage(message.getUUID(),channel);

    Assertions.assertThat(messageService.getMessagesByChannel(channel)).hasSize(0);
  }

}
