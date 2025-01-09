package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // 1. UserService 테스트
        System.out.println("== 사용자(UserService) 테스트 ==");
        JCFUserService userService = new JCFUserService();

        // 사용자 생성
        User user = new User("장건희", "gunhee0760@gmail.com");
        userService.create(user);
        System.out.println("생성된 사용자: " + userService.read(user.getId()));

        // 사용자 수정
        user.updateUsername("AliceUpdated");
        userService.update(user.getId(), user);
        System.out.println("수정된 사용자: " + userService.read(user.getId()));

        // 사용자 삭제
        userService.delete(user.getId());
        System.out.println("삭제된 사용자 확인: " + userService.read(user.getId()));

        // 2. ChannelService 테스트
        System.out.println("\n== 채널(ChannelService) 테스트 ==");
        JCFChannelService channelService = new JCFChannelService();

        // 채널 생성
        Channel channel = new Channel("General", "일반 토론 채널");
        channelService.create(channel);
        System.out.println("생성된 채널: " + channelService.read(channel.getId()));

        // 채널 수정
        channel.updateDescription("업데이트된 일반 토론 채널");
        channelService.update(channel.getId(), channel);
        System.out.println("수정된 채널: " + channelService.read(channel.getId()));

        // 채널 삭제
        channelService.delete(channel.getId());
        System.out.println("삭제된 채널 확인: " + channelService.read(channel.getId()));

        // 3. MessageService 테스트
        System.out.println("\n== 메시지(MessageService) 테스트 ==");
        JCFMessageService messageService = new JCFMessageService();

        // 메시지 생성
        UUID senderId = UUID.randomUUID(); // 가상의 발신자 ID
        UUID channelId = UUID.randomUUID(); // 가상의 채널 ID
        Message message = new Message("안녕하세요, 세계!", senderId, channelId);
        messageService.create(message);
        System.out.println("생성된 메시지: " + messageService.read(message.getId()));

        // 메시지 수정
        message.updateContent("안녕하세요, 업데이트된 세계!");
        messageService.update(message.getId(), message);
        System.out.println("수정된 메시지: " + messageService.read(message.getId()));

        // 메시지 삭제
        messageService.delete(message.getId());
        System.out.println("삭제된 메시지 확인: " + messageService.read(message.getId()));
    }
}
