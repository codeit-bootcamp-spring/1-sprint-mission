package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        //------ User ------
        UserService userService = new JCFUserService();

        //등록
        userService.createUser("1234", "Alice");
        userService.createUser("1234", "Bob");
        userService.createUser("1234", "Chris");
        userService.createUser("1234", "David");

        //전체 조회
        System.out.println("유저 전체 조회");
        userService.findAllUsers().forEach(user -> System.out.println(user.getName()));

        //단건 조회
        userService.findUserByName("Alice")
            .ifPresentOrElse(user ->
                System.out.println("유저 단건 조회: " + user.getName()), () -> {
                throw new IllegalArgumentException("user not found");
            });

        //유저 이름 업데이트
        userService.updateUserName(userService.findAllUsers().get(0).getId(), "Alice2");
        userService.findAllUsers().forEach(user -> System.out.println("\n유저 이름 업데이트: " + user.getName() + user.getUpdatedAt()));

        //유저 비밀번호 업데이트
        userService.updateUserPassword(userService.findAllUsers().get(0).getId(), "2345");

        //유저 삭제
        userService.removeUser(userService.findAllUsers().get(0).getId());
        userService.findAllUsers().forEach(user -> System.out.println("유저 삭제: " + user.getName()));

        //------ Channel ------
        ChannelService channelService = new JCFChannelService();

        //등록
        channelService.createChannel("Alice's Channel", userService.findAllUsers());
        channelService.createChannel("Bob's Channel", List.of(userService.findAllUsers().get(0), userService.findAllUsers().get(1)));
        channelService.createChannel("Chris's Channel", List.of(userService.findAllUsers().get(1), userService.findAllUsers().get(2)));

        //전체 조회
        System.out.println("\n채널 전체 조회");
        channelService.findAllChannels().forEach(channel -> System.out.println(channel.getName()));

        //단건 조회
        System.out.println("\n채널 단건 조회");
        UUID channelId = channelService.findAllChannels().get(0).getId();
        channelService.findChannel(channelId)
            .ifPresentOrElse(channel -> System.out.println(channel.getName()), () -> {
                throw new IllegalArgumentException("channel not found" + channelId);
            });

        //채널 이름 업데이트
        System.out.println("\n채널 이름 업데이트");
        channelService.updateChannelName(channelService.findAllChannels().get(0).getId(), "Alice's Channel2");
        channelService.findAllChannels().forEach(channel -> System.out.println(channel.getName()));

        //멤버 업데이트
        System.out.println("\n멤버 업데이트");
        channelService.updateMember(channelService.findAllChannels().get(0).getId(), List.of(userService.findAllUsers().get(0), userService.findAllUsers().get(1)));
        channelService.findChannel(channelService.findAllChannels().get(0).getId())
            .ifPresent(channel -> channel.getMembers().forEach(user -> System.out.println(user.getName())));

        //채널 삭제
        System.out.println("\n채널 삭제");
        channelService.removeChannel(channelService.findAllChannels().get(0).getId());
        channelService.findAllChannels().forEach(channel -> System.out.println(channel.getName()));

        //------ Message ------
        MessageService messageService = new JCFMessageService();

        //메시지 생성
        messageService.createCommonMessage(userService.findAllUsers().get(0), "Hello");
        messageService.createReplyMessage(userService.findAllUsers().get(1), "Hi");

        //메시지 전체 조회
        System.out.println("\n메시지 전체 조회");
        messageService.findAllMessages().forEach(message -> System.out.println(message.getContent()));

        //메시지 단건 조회
        System.out.println("\n메시지 단건 조회");
        System.out.println(messageService.findAllMessages().get(0).getContent());

        //메시지 업데이트
        System.out.println("\n메시지 업데이트");
        messageService.updateMessage(messageService.findAllMessages().get(0).getId(), "Hello2");
        messageService.findAllMessages().forEach(message -> System.out.println(message.getContent()));


        //메시지 삭제
        System.out.println("\n메시지 삭제");
        messageService.removeMessage(messageService.findAllMessages().get(0).getId());
        messageService.findAllMessages().forEach(message -> System.out.println(message.getContent()));


        //------ Channel에 메시지 전송 ------
        User user1 = userService.findAllUsers().get(0);
        User user2 = userService.findAllUsers().get(1);
        Channel channel = channelService.findAllChannels().get(0);

        Message message1 = Message.ofCommon(user1, "Good morning");
        Message message2 = Message.ofReply(user2, "Good bye"); // reply는 답장 대상을 받도록 수정 필요
        channelService.sendMessage(channel.getId(), message1);
        channelService.sendMessage(channel.getId(), message2);

        System.out.println("\n" + channel.getName() + " 채널의 메세지 : ");
        channel.getMessages().forEach(message -> System.out.println("(" + message.getSender().getName() + ") " + message.getContent()));

    }
}
