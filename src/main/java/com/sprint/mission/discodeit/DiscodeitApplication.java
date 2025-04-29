package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class DiscodeitApplication {


  public static void main(String[] args) {
    //Spring의 애플리케이션 컨텍스트 인터페이스로, 빈의 생성과 관계 설정을 담당한다.
    ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class,
        args);

//    //서비스 초기화, context에서 각 서비스 빈을 가져온다.
//    UserService userService = context.getBean(UserService.class);
//    ChannelService channelService = context.getBean(ChannelService.class);
//    MessageService messageService = context.getBean(MessageService.class);
//    BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
//    UserStatusService userStatusService = context.getBean(UserStatusService.class);
//    ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
//
//    //셋업
//    User user = userService.createUser(
//        new UserCreateDTO("홍길동", "1234", "dis@code.it", "filePath_gildong"));
//    User user2 = userService.createUser(
//        new UserCreateDTO("김이박", "5678", "dis2@code.it", "filePath_kim"));
//
//    List<String> privateUserList = new ArrayList<>();
//
//    privateUserList.add(user2.getId().toString());
//
//    Channel PBChannel = channelService.createPublicChannel(new ChannelCreateDTO("공개 채널"));
//    Channel PVChannel = channelService.createPrivateChannel(
//        new PrivateChannelCreateDTO("비공개 채널", privateUserList));
//
//    System.out.println(PBChannel.getId());
//    System.out.println(user2.getId());
//
//    Message message = messageService.createMessage(
//        new MessageCreateDTO(user.getId(), PBChannel.getId(), "hello", null));
//    Message message2 = messageService.createMessage(
//        new MessageCreateDTO(user2.getId(), PBChannel.getId(), "what up", null));

  }

}
