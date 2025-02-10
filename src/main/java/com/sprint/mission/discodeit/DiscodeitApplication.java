package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.FindChannelDto;
import com.sprint.mission.discodeit.dto.message.SendMessageDto;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static com.sprint.mission.discodeit.JavaApplication.messageCreateTest;
import static com.sprint.mission.discodeit.JavaApplication.setupChannel;

@SpringBootApplication
public class DiscodeitApplication {
  
  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
    
    UserService userService = context.getBean(UserService.class);
    ChannelService channelService = context.getBean(ChannelService.class);
    MessageService messageService = context.getBean(MessageService.class);
    
    // 셋업
    FindUserDto user1 = JavaApplication.setupUser(userService);
    FindChannelDto channel = setupChannel(channelService, user1.id());
    // 테스트
    SendMessageDto messageDto = new SendMessageDto(channel.id(), user1.id(), null, null, "test message content");
    messageCreateTest(messageService, messageDto);
    
  }
  
}
