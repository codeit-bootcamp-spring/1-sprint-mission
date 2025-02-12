package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.file.FileUserService;
//import com.sprint.mission.discodeit.service.file.FileChannelService;
//import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
//import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;



public class JavaApplication {
  public static void main(String[] args) {
    // 서비스 초기화 변수/타입 인터페이스로 하는 건지 구현체로 하는 건지 헷갈
    ChannelRepository channelRepository = new JCFChannelRepository();
    ChannelService channelService = new JCFChannelService(channelRepository);

    UserRepository userRepository = new JCFUserRepository();
    UserService userService = new JCFUserService(userRepository);

    // ===== 회원 =====
    System.out.println("\n===== 회원 서비스 CRUD =====");
    // 등록
    User user1 = new User("정연경", 24, Gender.FEMALE);
    userService.createUser(user1);

    User user2 = new User("신서연", 23, Gender.FEMALE);
    userService.createUser(user2);

    // 조회 - 단건
    userService.readUser(user1.getId());

    // 조회 - 다건
    userService.readAllUsers();

    // 수정 & 조회
    userService.updateUser(user1.getId(), "정연경", 23, Gender.FEMALE);

    // 삭제 & 조회
    userService.deleteUser(user1.getId());


    // ===== 채널 =====
    System.out.println("\n===== 채널 서비스 CRUD =====");
    // 등록
    Channel ch1 = new Channel("general", "General discussion");
    channelService.createChannel(ch1);

    Channel ch2 = new Channel("extra", "Extra discussion");
    channelService.createChannel(ch2);

    // 조회 - 단건
    channelService.readChannel(ch1.getId());

    // 조회 - 다건
    channelService.readAllChannels();

    // 수정 & 조회
    channelService.updateChannel(ch1.getId(), "수정된 채널", "General discussion"); // 이름이랑 내용 중 하나만 수정하려면 매개변수 어떡하지 ?

    // 삭제 & 조회
    channelService.deleteChannel(ch1.getId());


//    // ===== 메세지 =====
//    System.out.println("\n===== 메세지 서비스 CRUD =====");
//
//    // 메세지를 보낼 회원 등록
//    User messageUser = new User("MessageSender", 25,MALE);
//    userService.createUser(messageUser);
//
//    // 등록
//    Message message = new Message("안녕하세요", messageUser.getId());
//    if (messageService.createMessage(message)) {
//      System.out.println("등록된 메세지: " + message);
//    } else {
//      System.out.println("메세지 등록 실패");
//    }
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