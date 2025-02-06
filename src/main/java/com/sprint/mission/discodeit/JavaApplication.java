package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;


public class JavaApplication {
    static void userCRUDTest(UserService userService) {
        // 생성
        UserCreateRequest request = new UserCreateRequest(
                "이병규",
                "buzzin2426@gmail.com",
                "01012341234",
                "qwer!@34",
                null // 프로필 이미지가 없을 경우
        );

//        User user = userService.create(request);
//        System.out.println("유저 생성: " + user.getId());
//        // 조회
//        User foundUser = userService.findById(user.getId());
//        System.out.println("유저 조회(단건): " + foundUser.getId());
//        List<User> foundUsers = userService.findAll();
//        System.out.println("유저 조회(다건): " + foundUsers.size());
//        // 수정
//        User updatedUser = userService.update(user.getId(), "홍길동", "example@gmail.com", "01056785678", "asdf12#$");
//        System.out.println("유저 수정: 이름: " + updatedUser.getUsername() + ", 이메일: " + updatedUser.getEmail() + ", 번호: " + updatedUser.getPhoneNumber());
//        // 삭제
//        userService.delete(user.getId());
//        List<User> foundUsersAfterDelete = userService.findAll();
//        System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
    }

//    static void channelCRUDTest(ChannelService channelService) {
//        // 생성
//        Channel channel = channelService.create("코드잇", "코드잇 스프린트", ChannelType.PUBLIC);
//        System.out.println("채널 생성: " + channel.getId());
//        // 조회
//        Channel foundChannel = channelService.findById(channel.getId());
//        System.out.println("채널 조회(단건): " + foundChannel.getId());
//        List<Channel> foundChannels = channelService.findAll();
//        System.out.println("채널 조회(다건): " + foundChannels.size());
//        // 수정
//        Channel updateChannel = channelService.update(channel.getId(), "스프링", "자바", ChannelType.PRIVATE);
//        System.out.println("채널 수정: 이름: " + updateChannel.getName() + ", 설명: " + updateChannel.getDescription() + ", 타입: " + updateChannel.getChannelType());
//        //삭제
//        channelService.delete(channel.getId());
//        List<Channel> foundChannelAfterDelete = channelService.findAll();
//        System.out.println("채널 삭제: " + foundChannelAfterDelete.size());
//    }

//    static void messageCRUDTest(MessageService messageService) {
//        // 생성
//        UUID channelId = UUID.randomUUID();
//        UUID authorId = UUID.randomUUID();
//        Message message = messageService.create("안녕하세요.", channelId, authorId);
//        System.out.println("메시지 생성: " + message.getId());
//        // 조회
//        Message foundMessage = messageService.findById(message.getId());
//        System.out.println("메시지 조회(단건): " + foundMessage.getId());
//        List<Message> foundMessages = messageService.findAll();
//        System.out.println("메시지 조회(다건): " + foundMessages.size());
//        // 수정
//        Message updatedMessage = messageService.update(message.getId(), "반갑습니다.");
//        System.out.println("메시지 수정: " + updatedMessage.getContent());
//        // 삭재
//        messageService.delete(message.getId());
//        List<Message> foundMessagesAfterDelete = messageService.findAll();
//        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
//    }

    public static void main(String[] args) {

//        UserService userService = new BasicUserService(new FileUserRepository(), new JCFUserStatusRepository());
//        ChannelService channelService = new BasicChannelService(new FileChannelRepository());
//        MessageService messageService = new BasicMessageService(new FileMessageRepository());
//
//        //userCRUDTest(userService);
//        System.out.println();
//        //channelCRUDTest(channelService);
//        System.out.println();
//        messageCRUDTest(messageService);
    }
}