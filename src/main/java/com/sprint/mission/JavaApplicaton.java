package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.stream.Collectors;


public class JavaApplicaton {
    public static void main(String[] args) {

        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // 1. 등록
        User user1 = new User("user1", "user1@example.com", "01012345678");
        User user2 = new User("user2", "user2@example.com", "01087654321");
        userService.create(user1);
        userService.create(user2);

        Channel channel1 = new Channel("Sprint");
        Channel channel2 = new Channel("codeit");
        channelService.create(channel1);
        channelService.create(channel2);

        Message message1 = new Message("Hello world!", user1);
        Message message2 = new Message("Hello java!", user2);
        messageService.create(message1);
        messageService.create(message2);

        // 2. 조회 (단건, 다건)
        System.out.println("User 조회");
        User searchUser = userService.readById(user1.getId());
        System.out.println("id로 유저 찾기: " + searchUser.getUsername());

        String searchAllUser = userService.readAll().stream()
                .map(User::getUsername)
                .collect(Collectors.joining(", "));
        System.out.println("모든 유저: " + searchAllUser + "\n");


        System.out.println("Channel 조회");
        Channel searchChannel = channelService.readById(channel1.getId());
        System.out.println("id로 채널 찾기: " + searchChannel.getName());

        String searchAllChannel = channelService.readAll().stream()
                .map(Channel::getName)
                .collect(Collectors.joining(", "));
        System.out.println("모든 채널: " + searchAllChannel + "\n");

        System.out.println("Message 조회");
        Message searchMessage = messageService.readById(message1.getId());
        System.out.println("id로 메시지 찾기: " + searchMessage.getContent());

        String searchAllMessage = messageService.readAll().stream()
                .map(Message::getContent)
                .collect(Collectors.joining(", "));
        System.out.println("모든 메시지: " + searchAllMessage + "\n");

        // 3. 수정
        user2.update("user2 update", "user2_update", "user2_update@example.com", "99999999");
        channel2.update("channel2 update");
        message2.update("message2 update");

        // 4. 수정된 데이터 조회
        System.out.println("수정 확인");
        User updatedUser = userService.readById(user2.getId());
        System.out.println("수정된 유저 조회: " + updatedUser.getUsername());

        Channel updatedChannel = channelService.readById(channel2.getId());
        System.out.println("수정된 채널 조회: " + updatedChannel.getName());

        Message updatedMessage = messageService.readById(message2.getId());
        System.out.println("수정된 메시지 조회: " + updatedMessage.getContent() + "\n");

        // 5. 삭제
        userService.delete(user2.getId());
        channelService.delete(channel2.getId());
        messageService.delete(message2.getId());

        // 6. 삭제 확인
        System.out.println("삭제 획인");
        searchAllUser = userService.readAll().stream()
                .map(User::getUsername)
                .collect(Collectors.joining(", "));
        System.out.println("모든 유저: " + searchAllUser);

        searchAllChannel = channelService.readAll().stream()
                .map(Channel::getName)
                .collect(Collectors.joining(", "));
        System.out.println("모든 채널: " + searchAllChannel);

        searchAllMessage = messageService.readAll().stream()
                .map(Message::getContent)
                .collect(Collectors.joining(", "));
        System.out.println("모든 메시지: " + searchAllMessage);
    }
}