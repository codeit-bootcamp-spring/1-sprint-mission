
package com.sprint.mission;

import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sprint.mission.discodeit.*", "com.sprint.mission.discodeit.repository.jcf"})
public class DiscodeitApplication {

  private static AuthService authService;
  private static ChannelService channelService;
  private static MessageService messageService;
  private static UserService userService;
  private static BinaryContentService binaryContentService;
  private static ReadStatusService readStatusService;
  private static UserStatusService userStatusService;

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
    authService = context.getBean(AuthService.class);
    channelService = context.getBean(ChannelService.class);
    messageService = context.getBean(MessageService.class);
    userService = context.getBean(UserService.class);
    binaryContentService = context.getBean(BinaryContentService.class);
    readStatusService = context.getBean(ReadStatusService.class);
    userStatusService = context.getBean(UserStatusService.class);

  }

  static void userSimulation() {

  }
}
/*

  static void userSimulation() {

    System.out.println("=== 유저 생성 및 조회 ===");
    CreateUserDto user = null;
    CreateUserDto user2 = null;

    try {
      user = new CreateUserDto(
          "user1",
          "examplePwd",
          "example@gmail.com",
          "exampleNickname",
          "01012345678",
          null,
          null,
          null,
          "this is example description");

    } catch (UserValidationException e) {
      System.out.println(e.getMessage());
    }

    try {
      user2 = new CreateUserDto(
          "user2",
          "examplePwd2",
          "example2@gmail.com",
          "exampleNick2",
          "01013345678",
          null,
          null,
          null,
          "this is example description2"
      );
    } catch (UserValidationException e) {
      System.out.println(e.getMessage());
    }

    User createdUser = userService.createUser(user);
    User createdUser2 = userService.createUser(user2);

    // 유저 조회
    String userId = createdUser.getUUID();
    UserResponseDto fetchedUser = userService.findUserById(userId);

    System.out.println(fetchedUser);

    // 여러 유저 조회
    List<UserResponseDto> users = userService.findAllUsers();

    System.out.println("\n=== 여러 유저 조회 ===");

    for (UserResponseDto u : users) {
      System.out.println(u);
    }

    // 유저 정보 업데이트
    System.out.println("\n=== 유저 정보 업데이트 및 기존 유저와 동등성 ===");
    UserUpdateDto updateDto = new UserUpdateDto("updated", null,null,null,null,null,null,null,null);

    userService.updateUser(userId, updateDto, "examplePwd");

    // 업데이트 유저 출력

    User updatedUser = userService.findUserById(userId).get();

    System.out.println("USERs : " + userService.findAllUsers().size());
    System.out.println(fetchedUser);

    System.out.println(fetchedUser == updatedUser);

    System.out.println("\n=== 유저 데이터 조작 비밀번호 오류 ===");
    // 비밀번호 틀릴시 업데이트 x
    try {
      userService.updateUser(userId, updateDto, "1");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    // 유저 삭제 비밀번호 틀립
    try {
      userService.deleteUser(userId, "examplePwdㅁ");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    System.out.println("\n=== 삭제 후 유저 x ===");
    // 유저 삭제 비밀번호 동일
    try {
      userService.deleteUser(userId, "examplePwd");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("USERs : " + userService.findAllUsers().size());
    System.out.println(userService.findUserById(userId).isEmpty());
  }

  static void channelSimulation2() {

    System.out.println("\n=== 채널 생성 / 조회 ===");

    ChatChannel chatChannel = null;

    try {
      chatChannel = channelFactory.createChatChannel(
          "server-uuid-1",
          "category-uuid-1",
          "exampleChatName",
          100
      );
    } catch (ChannelValidationException e) {
      System.out.println(e.getMessage());
    }


    VoiceChannel voiceChannel = null;

    try {
      voiceChannel = channelFactory.createVoiceChannel(
          "server-uuid-1",
          "category-uuid-1",
          "exampleVoiceName",
          false
      );
    } catch (ChannelValidationException e) {
      System.out.println(e.getMessage());
    }


    String chatChannelId = chatChannel.getUUID();
    String voiceChannelId = voiceChannel.getUUID();

    channelService.createChannel(chatChannel);
    channelService.createChannel(voiceChannel);
    System.out.println(channelService.getChannelById(chatChannelId).get());

    System.out.println("\n=== 모든 채널 조회 ===");
    List<BaseChannel> channels = channelService.getAllChannels();
    channels.stream().forEach(System.out::println);

    System.out.println("\n=== 채널 업데이트 ===");
    ChannelUpdateDto updatedChannel = new ChannelUpdateDto(
        Optional.of("updatedChannelName"),
        Optional.empty(),
        Optional.empty(),
        Optional.empty()
    );

    channelService.updateChannel(chatChannelId, updatedChannel);
    System.out.println(channelService.getChannelById(chatChannelId).get());

    System.out.println("\n=== 음성 채널 삭제 후 전채 조회 ===");
    channelService.deleteChannel(voiceChannelId);

    List<BaseChannel> channels2 = channelService.getAllChannels();
    channels2.stream().forEach(System.out::println);

  }

  static void messageSimulation2() {

    System.out.println("\n=== 메시지를 보낼 유저와 채널 생성 ===");

    CreateUserDto user = null;

    try {
      user = new CreateUserDto(
          "user3",
          "examplePwd3",
          "example3@gmail.com",
          "exampleNick3",
          "01013345678",
          null,
          null,
          null,
          "this is example description3"
      );
    } catch (UserValidationException e) {
      System.out.println(e.getMessage());
    }

    User createdUser = userService.createUser(user);

    ChatChannel chatChannel = null;
    try {
      chatChannel = channelFactory.createChatChannel(
          "server-uuid-2",
          "category-uuid-2",
          "exampleChannelName",
          100
      );
    } catch (ChannelValidationException e) {
      System.out.println(e.getMessage());
    }

    ChatChannel chatChannel2 = null;

    try {
      chatChannel2 = channelFactory.createChatChannel(
          "server-uuid-22",
          "category-uuid-22",
          "exampleChannelName2",
          100
      );
    } catch (ChannelValidationException e) {
      System.out.println(e.getMessage());
    }

    channelService.createChannel(chatChannel);
    channelService.createChannel(chatChannel2);

    Message message = null;

    try {
      message = new Message.MessageBuilder(
          createdUser.getUUID(),
          chatChannel.getUUID(),
          "this is first Chat"
      ).build();
    } catch (MessageValidationException e) {
      System.out.println(e.getMessage());
    }


    try {
      messageServiceV2.createMessage(createdUser.getUUID(), message, chatChannel);
      messageServiceV2.createMessage(createdUser.getUUID(), new Message.MessageBuilder(
          createdUser.getUUID(),
          chatChannel.getUUID(),
          "this is second Chat"
      ).build(), chatChannel);
    } catch (MessageValidationException e) {
      System.out.println(e.getMessage());
    }

    try {
      messageServiceV2.createMessage(createdUser.getUUID(), new Message.MessageBuilder(
          createdUser.getUUID(),
          chatChannel.getUUID(),
          "this is third Chat"
      ).build(), chatChannel);
    } catch (MessageValidationException e) {
      System.out.println(e.getMessage());
    }

    try {
      messageServiceV2.createMessage(createdUser.getUUID(), new Message.MessageBuilder(
          createdUser.getUUID(),
          chatChannel2.getUUID(),
          "this is second channel first Chat"
      ).build(), chatChannel2);
    } catch (MessageValidationException e) {
      System.out.println(e.getMessage());
    }

    Optional<Message> optionalMessage = messageServiceV2.getMessageById(message.getUUID(), chatChannel);
    Message m = optionalMessage.orElseThrow(() -> new NoSuchElementException("Message not found"));
    System.out.println(m);

    System.out.println("\n=== 1번 채널 채팅 내역 ===");
    messageServiceV2.getMessagesByChannel(chatChannel).stream().forEach(System.out::println);

    System.out.println("\n=== 2번 채널 채팅 내역 ===");
    messageServiceV2.getMessagesByChannel(chatChannel2).stream().forEach(System.out::println);

    System.out.println("\n=== 메시지 업데이트 ===");
    MessageUpdateDto updateMessage = new MessageUpdateDto(Optional.of("this is updated content"), Optional.empty());
    messageServiceV2.updateMessage(chatChannel, message.getUUID(), updateMessage);
    System.out.println(messageServiceV2.getMessageById(message.getUUID(), chatChannel));

    System.out.println("\n=== 1번 채널 채팅 삭제 ===");
    messageServiceV2.deleteMessage(message.getUUID(), chatChannel);
    messageServiceV2.getMessagesByChannel(chatChannel).stream().forEach(System.out::println);

    System.out.println("\n=== 존재하지 않는 유저의 메시지 생성 ===");

    try {
      String falseUserUUID = "false-id";
      Message falseUserMessage = new Message.MessageBuilder(
          "false-user-uuid",
          chatChannel.getUUID(),
          "false"
      ).build();
      messageServiceV2.createMessage(falseUserUUID, falseUserMessage, chatChannel);
    } catch (MessageValidationException e) {
      System.out.println(e.getMessage());
    }
  }
}
*/
