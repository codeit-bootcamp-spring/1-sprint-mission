package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.FindChannelDto;
import com.sprint.mission.discodeit.dto.message.SendMessageDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.util.FileIO;
import com.sprint.mission.discodeit.validation.UserValidator;
import java.util.List;
import java.util.UUID;

public class JavaApplication {
  static FindUserDto setupUser(UserService userService) {
    CreateUserDto createUserDto = new CreateUserDto("tltl21", "codeIt", "aaa@a.a", null);
    userService.createUser(createUserDto);
    return userService.findByName("codeIt");
  }
  
  static FindChannelDto setupChannel(ChannelService channelService, UUID ownerId) {
    channelService.createPublicChannel(ownerId, "testChannel", "test");
    return channelService.findByName("testChannel");
  }
  
  static void messageCreateTest(MessageService messageService, SendMessageDto messageDto) {
    messageService.sendCommonMessage(messageDto);
    List<Message> messages = messageService.findBySender(messageDto.senderId());
    System.out.println("메시지 생성: " + messages);
  }
  
  public static void main(String[] args) {
    FileIO fileIO = new FileIO();
    Encryptor encryptor = new Encryptor();
    ReadStatusRepository readStatusRepository = new FileReadStatusRepository(fileIO);
    UserRepository userRepository = new FileUserRepository(fileIO);
    MessageRepository messageRepository = new FileMessageRepository(fileIO, userRepository);
    ChannelRepository channelRepository = new FileChannelRepository(fileIO, userRepository, messageRepository);
    UserStatusRepository userStatusRepository = new FileUserStatusRepository(fileIO);
    BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository(fileIO);
    
    UserValidator userValidator = new UserValidator(userRepository);
    
    // 서비스 초기화
    UserService userService = new BasicUserService(userRepository, userStatusRepository, encryptor, userValidator, binaryContentRepository);
    ChannelService channelService = new BasicChannelService(channelRepository, userRepository, readStatusRepository, messageRepository);
    MessageService messageService = new BasicMessageService(messageRepository, binaryContentRepository);
    
  }
}

