package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {

        // UserService 테스트
            JCFUserService userService = new JCFUserService();
        User user1 = userService.create(new User("Alice", "alice@example.com"));
        User user2 = userService.create(new User("Bob", "bob@example.com"));

        System.out.println("\n[User 등록 후 조회]");
        System.out.println("모든 사용자: " + userService.readAll());

        System.out.println("\n[단일 사용자 조회]");
        System.out.println("User1: " + userService.read(user1.getId()));

        System.out.println("\n[User 수정]");
        userService.update(user1.getId(), "AliceUpdated", "alice.updated@example.com");
        System.out.println("수정된 User1: " + userService.read(user1.getId()));

        System.out.println("\n[User 삭제]");
        userService.delete(user2.getId());
        System.out.println("모든 사용자(삭제 후): " + userService.readAll());

        // ChannelService 테스트
        JCFChannelService channelService = new JCFChannelService();
        Channel channel1 = channelService.create(new Channel("General", "Text"));
        Channel channel2 = channelService.create(new Channel("Voice Chat", "Voice"));

        System.out.println("\n[Channel 등록 후 조회]");
        System.out.println("모든 채널: " + channelService.readAll());

        System.out.println("\n[단일 채널 조회]");
        System.out.println("Channel1: " + channelService.read(channel1.getId()));

        System.out.println("\n[Channel 수정]");
        channelService.update(channel1.getId(), "General Updated", "Text");
        System.out.println("수정된 Channel1: " + channelService.read(channel1.getId()));

        System.out.println("\n[Channel 삭제]");
        channelService.delete(channel2.getId());
        System.out.println("모든 채널(삭제 후): " + channelService.readAll());

        // MessageService 테스트
        JCFMessageService messageService = new JCFMessageService();
        Message message1 = messageService.create(new Message("Hello World!", user1));
        Message message2 = messageService.create(new Message("How are you?", user1));

        System.out.println("\n[Message 등록 후 조회]");
        System.out.println("모든 메시지: " + messageService.readAll());

        System.out.println("\n[단일 메시지 조회]");
        System.out.println("Message1: " + messageService.read(message1.getId()));

        System.out.println("\n[Message 수정]");
        messageService.update(message1.getId(), "Hello Updated World!");
        System.out.println("수정된 Message1: " + messageService.read(message1.getId()));

        System.out.println("\n[Message 삭제]");
        messageService.delete(message2.getId());
        System.out.println("모든 메시지(삭제 후): " + messageService.readAll());
    }
}
