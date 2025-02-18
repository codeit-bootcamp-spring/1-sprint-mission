package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.FindChannelDto;
import com.sprint.mission.discodeit.dto.message.SendMessageDto;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FileIO;
import com.sprint.mission.discodeit.validation.UserValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static com.sprint.mission.discodeit.JavaApplication.messageCreateTest;
import static com.sprint.mission.discodeit.JavaApplication.setupChannel;

@SpringBootApplication
public class DiscodeitApplication {
  
  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
    
    ReadStatusRepository readStatusRepository = context.getBean(ReadStatusRepository.class);
    UserRepository userRepository = context.getBean(UserRepository.class);
    MessageRepository messageRepository = context.getBean(MessageRepository.class);
    ChannelRepository channelRepository = context.getBean(ChannelRepository.class);
    UserStatusRepository userStatusRepository = context.getBean(UserStatusRepository.class);
    BinaryContentRepository binaryContentRepository = context.getBean(BinaryContentRepository.class);
    UserService userService = context.getBean(UserService.class);
    ChannelService channelService = context.getBean(ChannelService.class);
    MessageService messageService = context.getBean(MessageService.class);
    FileIO fileIO = new FileIO();
    Encryptor encryptor = new Encryptor();
    UserValidator userValidator = new UserValidator(userRepository);
    
    // 셋업
    FindUserDto user1 = JavaApplication.setupUser(userService);
    FindChannelDto channel = setupChannel(channelService, user1.id());
    // 테스트
    SendMessageDto messageDto = new SendMessageDto(channel.id(), user1.id(), null, null, "test message content");
    messageCreateTest(messageService, messageDto);
    
  }
  
}
