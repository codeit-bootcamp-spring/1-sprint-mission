package com.sprint.mission;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {

    private static UserService userService = JCFUserService.getInstance();
    private static ChannelService channelService = JCFChannelService.getInstance();
    private static MessageService messageService = JCFMessageService.getInstance();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("===== userServiceTest ====");
        System.out.println();

        System.out.println("===== user 생성 =====");
        User user1 = userService.createUser(new UserDto("test1", "test1", "허원재"));
        User user2 = userService.createUser(new UserDto("test2", "test2", "이규석"));
        User user3 = userService.createUser(new UserDto("test3", "test3", "임수빈"));

        System.out.println("===== 생성된 user =====");
        userService.readAll().stream().forEach(System.out::println);
        System.out.println();

        System.out.println("===== user1의 정보 =====");
        System.out.println(userService.readUser(user1.getId()));
        System.out.println();

        System.out.println("===== user1의 loginId와 password를 변경 =====");
        userService.updateUser(user1.getId(), new UserDto("asdf", "qwer", "허원재"));
        user1 = userService.readUser(user1.getId());
        System.out.println(user1);
        System.out.println();

        System.out.println("===== user2 삭제 =====");
        userService.deleteUser(user2.getId());
        System.out.println("===== 등록된 user =====");
        userService.readAll().stream().forEach(System.out::println);
        System.out.println();




        System.out.println("===== channelServiceTest =====\n");

        System.out.println("===== channel 생성 =====");
        Channel codeit = channelService.createChannel("codeit");
        Channel spring = channelService.createChannel("spring");

        System.out.println("===== 생성된 channel =====");
        channelService.readAll().stream().forEach(System.out::println);
        System.out.println();

        System.out.println("===== codeit channel 이름 변경 =====");
        channelService.updateName(codeit.getId(), "newCodeit");
        System.out.println(channelService.readChannel(codeit.getId()));
        System.out.println();

        System.out.println("===== spring channel에 user 추가 =====");
        channelService.addUser(spring.getId(), user3.getId());
        channelService.addUser(spring.getId(), user1.getId());
        System.out.println(channelService.readChannel(spring.getId()));
        System.out.println();

        System.out.println("===== spring channel의 user 삭제 =====");
        channelService.deleteUser(spring.getId(), user3.getId());
        System.out.println(channelService.readChannel(spring.getId()));
        System.out.println();

        System.out.println("===== codeit channel 삭제 =====");
        channelService.deleteChannel(codeit.getId());
        System.out.println("===== 등록된 channel 리스트 =====");
        channelService.readAll().stream().forEach(System.out::println);
        System.out.println();



        System.out.println("===== messageServiceTest =====\n");

        System.out.println("===== message 생성 =====");
        Message message1 = messageService.createMessage(new MessageDto(user1, "hi", spring));
        Message message2 = messageService.createMessage(new MessageDto(user1, "world", spring));
        System.out.println("===== 등록된 message =====");
        messageService.readAll().stream().forEach(System.out::println);
        System.out.println();

        System.out.println("===== 등록되지 않은 user거나 보내는 channel에 등록되지 않은 유저면 exception 발생 =====");
        System.out.println("===== createMessage(new MessageDto(user2, \"hello\", spring)) =====");
        try {
            Message message3 = messageService.createMessage(new MessageDto(user2, "hello", spring));
        } catch (RuntimeException e) {
            System.out.println(e);
        }
        System.out.println();

        System.out.println("===== 메세지 내용 수정 =====");
        messageService.updateMessage(message1.getId(), "hello");
        System.out.println(messageService.readMessage(message1.getId()));
        System.out.println();

        System.out.println("===== 메세지 삭제 =====");
        messageService.deleteMessage(message1.getId());
        System.out.println("===== 현재 등록된 message =====");
        messageService.readAll().stream().forEach(System.out::println);
        System.out.println();
    }

}