package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

public class JavaApplication {

    public static void main(String[] args) {
        // 의존성 주입 (Dependency Injection)
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(userService, channelService);

        // 사용자 등록
        User user1 = new User("Kimdoil", "pw12345678!");
        User user2 = new User("testName", "test12345678!");

        userService.addUser(user1);
        userService.addUser(user2);

        // 채널 등록
        Channel channel1 = new Channel("8팀", user1.getId());
        Channel channel2 = new Channel("General", user2.getId());

        channelService.addChannel(channel1);
        channelService.addChannel(channel2);

        // 메시지 등록
        Message message1 = new Message("8팀 김도일입니다.", user1.getId(), channel1.getId());
        Message message2 = new Message("테스트 계정입니다. 반갑습니다!", user2.getId(), channel2.getId());

        messageService.addMessage(message1);
        messageService.addMessage(message2);

        // 조회 (단건)
        System.out.println("조회 - 단건:");
        System.out.println("User1: " + userService.getUser(user1.getId()).getUsername());
        System.out.println("Channel1: " + channelService.getChannel(channel1.getId()).getName());
        System.out.println("Message1: " + messageService.getMessage(message1.getId()).getContent());

        // 조회 (다건)
        System.out.println("\n조회 - 다건:");
        System.out.println("All Users:");
        userService.getAllUsers().forEach(user -> System.out.println(user.getUsername()));

        System.out.println("\nAll Channels:");
        channelService.getAllChannels().forEach(channel -> System.out.println(channel.getName()));

        System.out.println("\nAll Messages:");
        messageService.getAllMessages().forEach(msg -> System.out.println(msg.getContent()));

        // 수정
        userService.updateUser(user1.getId(), "KimdoilUpdated", "newpw12345678!");
        channelService.updateChannel(channel1.getId(), "GeneralUpdated");
        messageService.updateMessage(message1.getId(), "Updated Message!");

        // 수정된 데이터 조회
        System.out.println("\n수정된 데이터 조회:");
        System.out.println("Updated User1: " + userService.getUser(user1.getId()).getUsername());
        System.out.println("Updated Channel1: " + channelService.getChannel(channel1.getId()).getName());
        System.out.println("Updated Message1: " + messageService.getMessage(message1.getId()).getContent());

        // 삭제
        userService.deleteUser(user2.getId());
        channelService.deleteChannel(channel2.getId());
        messageService.deleteMessage(message2.getId());

        // 삭제 후 조회
        System.out.println("\n삭제 후 조회:");
        System.out.println("All Users:");
        userService.getAllUsers().forEach(user -> System.out.println(user.getUsername()));

        System.out.println("\nAll Channels:");
        channelService.getAllChannels().forEach(channel -> System.out.println(channel.getName()));

        System.out.println("\nAll Messages:");
        messageService.getAllMessages().forEach(msg -> System.out.println(msg.getContent()));
    }
}
