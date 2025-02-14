package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.FindChannelDto;
import com.sprint.mission.discodeit.dto.message.SendMessageDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class JavaApplication {
  static FindUserDto setupUser(UserService userService) {
    CreateUserDto createUserDto = new CreateUserDto("tltl21", "testName", "aaa@gmail.com", null);
    userService.createUser(createUserDto);
    System.out.println("유저 생성: " + createUserDto.name() + " " + createUserDto.email() + " " + createUserDto.profileImage());
    return userService.findByName(createUserDto.name());
  }
  
  static FindChannelDto setupChannel(ChannelService channelService, UUID ownerId) {
    channelService.createPublicChannel(ownerId, "testChannel", "test");
    System.out.println("채널 생성: " + channelService.findByName("testChannel").name() + " " + channelService.findByName("testChannel").description() + " " + channelService.findByName("testChannel").memberIds());
    return channelService.findByName("testChannel");
  }
  
  static void messageCreateTest(MessageService messageService, SendMessageDto messageDto) {
    messageService.sendCommonMessage(messageDto);
    List<Message> messages = messageService.findBySender(messageDto.senderId());
    System.out.println("메시지 생성: ");
    messages.forEach(m -> System.out.println(m.getSenderId() + " " + m.getContent()));
  }
  
  public static void main(String[] args) {
  
  }
}

