package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.config.ApplicationConfig;
import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.ChannelValidationException;
import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.factory.ChannelFactory;
import com.sprint.mission.discodeit.factory.service.ServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.ServiceType;
import com.sprint.mission.discodeit.util.StorageType;
import lombok.extern.slf4j.Slf4j;


import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.*;

@Slf4j
public class JavaApplication {

  static ChannelService channelService;
  static UserService userService;
  static MessageServiceV2<ChatChannel> messageServiceV2;
  static ChannelFactory channelFactory;


  private static void setup() {
    File file = new File(MESSAGE_FILE);
    File file2 = new File(USER_FILE);
    File file3 = new File(CHANNEL_FILE);

    if (file.exists()) {
      file.delete();
    }
    if (file2.exists()) {
      file2.delete();
    }
    if (file3.exists()) {
      file3.delete();
    }
  }

  public static void main(String[] args) {

    ApplicationConfig ac = new ApplicationConfig.ApplicationConfigBuilder()
        .channelServiceType(ServiceType.BASIC)
        .channelStorageType(StorageType.JCF)
        .userServiceType(ServiceType.BASIC)
        .userStorageType(StorageType.JCF)
        .messageServiceType(ServiceType.BASIC)
        .messageStorageType(StorageType.JCF)
        .build();

    channelService = ServiceFactory.createChannelService(ac);
    userService = ServiceFactory.createUserService(ac);
    messageServiceV2 = ServiceFactory.createMessageService(ac, userService, channelService);

    channelFactory = new ChannelFactory();

    setup();

    userSimulation();
    channelSimulation2();
    messageSimulation2();
  }

  static void userSimulation() {
    // 유저 생성
    User user = null;
    User user2 = null;

    System.out.println("=== 유저 생성 및 조회 ===");
    try {
      user = new User.UserBuilder("user1", "examplePwd", "example@gmail.com")
          .nickname("exampleNickname")
          .phoneNumber("01012345678")
          .profilePictureURL("https://example-url.com")
          .description("this is example description")
          .build();
    } catch (UserValidationException e) {
      System.out.println(e.getMessage());
    }

    try {
      user2 = new User.UserBuilder("user2", "examplePwd2", "example2@gmail.com")
          .nickname("exampleNick2")
          .phoneNumber("01013345678")
          .description("this is example description2")
          .build();
    } catch (UserValidationException e) {
      System.out.println(e.getMessage());
    }

    userService.createUser(user);
    userService.createUser(user2);

    // 유저 조회
    String userId = user.getUUID();
    User fetchedUser = userService.readUserById(userId).get();

    System.out.println(fetchedUser);

    // 여러 유저 조회
    List<User> users = userService.readAllUsers();

    System.out.println("\n=== 여러 유저 조회 ===");

    for (User u : users) {
      System.out.println(u);
    }

    // 유저 정보 업데이트
    System.out.println("\n=== 유저 정보 업데이트 및 기존 유저와 동등성 ===");
    UserUpdateDto updateDto = new UserUpdateDto(
        Optional.of("changedUsername"),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty()
    );

    userService.updateUser(userId, updateDto, "examplePwd");

    // 업데이트 유저 출력

    User updatedUser = userService.readUserById(userId).get();

    System.out.println("USERs : " + userService.readAllUsers().size());
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
    System.out.println("USERs : " + userService.readAllUsers().size());
    System.out.println(userService.readUserById(userId).isEmpty());
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

    User user = null;

    try {
      user = new User.UserBuilder("exampleUsername2", "examplePwd2", "exaample2@gmail.com")
          .nickname("exampleNickname2")
          .phoneNumber("01011345678")
          .profilePictureURL("https://example-url.com2")
          .description("this is example description2")
          .build();
    } catch (UserValidationException e) {
      System.out.println(e.getMessage());
    }

    userService.createUser(user);

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
          user.getUUID(),
          chatChannel.getUUID(),
          "this is first Chat"
      ).build();
    } catch (MessageValidationException e) {
      System.out.println(e.getMessage());
    }

//        chatBehavior.setChannel(chatChannel);
//        chatBehavior2.setChannel(chatChannel2);


    try {
      messageServiceV2.createMessage(user.getUUID(), message, chatChannel);
      messageServiceV2.createMessage(user.getUUID(), new Message.MessageBuilder(
          user.getUUID(),
          chatChannel.getUUID(),
          "this is second Chat"
      ).build(), chatChannel);
    } catch (MessageValidationException e) {
      System.out.println(e.getMessage());
    }

    try {
      messageServiceV2.createMessage(user.getUUID(), new Message.MessageBuilder(
          user.getUUID(),
          chatChannel.getUUID(),
          "this is third Chat"
      ).build(), chatChannel);
    } catch (MessageValidationException e) {
      System.out.println(e.getMessage());
    }

    try {
      messageServiceV2.createMessage(user.getUUID(), new Message.MessageBuilder(
          user.getUUID(),
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
