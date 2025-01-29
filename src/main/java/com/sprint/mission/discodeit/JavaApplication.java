package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;



public class JavaApplication {
  public static void main(String[] args) {
    // 서비스 초기화
    UserRepository userRepository = new FileUserRepository();  // UserRepository 생성
    UserService userService = new FileUserService(userRepository);  // FileUserService 생성
//   ChannelService channelService = new FileChannelService();
//   MessageService messageService = new FileMessageService();

    // ===== 회원 =====
    System.out.println("===== 회원 서비스 CRUD =====");
    // 등록
    User user = new User("정연경", 24, 'F');

    System.out.println("등록된 회원: " + user);

    User user2 = new User("신서연", 23, 'F');
    userService.createUser(user2);
    System.out.println("등록된 회원: " + user2);

    // 조회 - 단건
    userService.readUser(user.getId())
        .ifPresent(u -> System.out.println("특정 회원 조회: " + u));

    // 조회 - 다건
    System.out.println("모든 회원 조회: " + userService.readAllUsers());

    // 수정
    userService.updateUser(user, "정연경", 23, 'F');
    userService.updateUser(user2, "신서연", 22, 'F');

    // 수정된 데이터 조회
    userService.readUser(user.getId())
        .ifPresent(u -> System.out.println("수정된 회원 조회: " + u));
    userService.readUser(user2.getId())
        .ifPresent(u -> System.out.println("수정된 회원 조회: " + u));

    // 삭제
    userService.deleteUser(user.getId());

    // 조회를 통해 삭제되었는지 확인
    System.out.println("삭제 후 회원 목록: " + userService.readAllUsers());


//    // ===== 채널 =====
//    System.out.println("\n===== 채널 서비스 CRUD =====");
//    // 등록
//    Channel channel = new Channel("general", "General discussion");
//    channelService.createChannel(channel);
//    System.out.println("등록된 채널: " + channel);
//
//    Channel channel2 = new Channel("general2", "General discussion");
//    channelService.createChannel(channel2);
//    System.out.println("등록된 채널: " + channel2);
//
//    // 조회 - 단건
//    channelService.readChannel(channel.getId())
//        .ifPresent(c -> System.out.println("특정 채널 조회: " + c));
//
//    // 조회 - 다건
//    System.out.println("모든 채널 조회: " + channelService.readAllChannels());
//
//    // 수정
//    channelService.updateChannel(channel.getId(), "수정된 채널", "Updated general discussion");
//
//    // 수정된 데이터 조회
//    channelService.readChannel(channel.getId())
//        .ifPresent(c -> System.out.println("수정된 채널 조회: " + c));
//
//    // 삭제
//    channelService.deleteChannel(channel.getId());
//
//    // 조회를 통해 삭제되었는지 확인
//    System.out.println("삭제 후 채널 목록: " + channelService.readAllChannels());
//
//
//    // ===== 메세지 =====
//    System.out.println("\n===== 메세지 서비스 CRUD =====");
//
//    // 메세지를 보낼 회원 등록
//    User messageUser = new User("MessageSender", 25, 'M');
//    userService.createUser(messageUser);
//
//    // 등록
//    Message message = new Message("안녕하세요", messageUser.getId());
//    messageService.createMessage(message);
//    System.out.println("등록된 메세지: " + message);
//
//    // 조회 - 단건
//    messageService.readMessage(message.getId())
//        .ifPresent(m -> System.out.println("특정 메세지 조회: " + m));
//
//    // 조회 - 다건
//    System.out.println("모든 메세지 조회: " + messageService.readAllMessages());
//
//    // 수정
//    messageService.updateMessage(message.getId(), "수정 문구: Hello", messageUser.getId());
//
//    // 수정된 데이터 조회
//    messageService.readMessage(message.getId())
//        .ifPresent(m -> System.out.println("수정된 메세지 조회: " + m));
//
//    // 삭제
//    messageService.deleteMessage(message.getId());
//
//    // 조회를 통해 삭제되었는지 확인
//    System.out.println("삭제 후 메세지 목록: " + messageService.readAllMessages());
  }
}