package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.List;

public class JavaApplication {
  static User setupUser(UserService userService) {
    userService.createUser("tltl21", "codeIt");
    return userService.findUserByName("codeIt");
  }
  
  static Channel setupChannel(ChannelService channelService, User owner, List<User> members) {
    channelService.createChannel("testChannel", owner, members);
    return channelService.findChannelByName("testChannel");
  }
  
  static void messageCreateTest(MessageService messageService, Channel channel, User author) {
    messageService.sendCommonMessage(channel, author, "Hello, World!");
    Message message = messageService.findMessagesBySender(author.getId()).get(0);
    System.out.println("메시지 생성: " + message.getContent());
  }
  
  public static void main(String[] args) {
    Encryptor encryptor = new Encryptor();
    UserRepository userRepository = new JCFUserRepository();
    ChannelRepository channelRepository = new JCFChannelRepository();
    MessageRepository messageRepository = new JCFMessageRepository();
    
    // 서비스 초기화
    // TODO Basic*Service 구현체를 초기화하세요.
    UserService userService = new BasicUserService(userRepository, encryptor);
    ChannelService channelService = new BasicChannelService(channelRepository);
    MessageService messageService = new BasicMessageService(messageRepository);
    
    // 셋업
    User user = setupUser(userService);
    Channel channel = setupChannel(channelService, user, List.of(user));
    // 테스트
    messageCreateTest(messageService, channel, user);
  }
}

