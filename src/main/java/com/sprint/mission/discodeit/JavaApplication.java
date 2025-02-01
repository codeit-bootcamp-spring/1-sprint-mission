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
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;

import java.time.Instant;
import java.util.Optional;

public class JavaApplication {
    public static void main(String[] args) {
        // JCF 기반 테스트
        System.out.println("== JCF 기반 테스트 ==");
        testService(
                JCFUserService.getInstance(),
                JCFChannelService.getInstance(),
                JCFMessageService.getInstance(
                        JCFUserService.getInstance(),
                        JCFChannelService.getInstance()
                )
        );

        // File IO 기반 테스트
        System.out.println("\n== File IO 기반 테스트 ==");
        testService(
                new FileUserService(),
                new FileChannelService(),
                new FileMessageService()
        );
    }

    private static void testService(
            UserService userService,
            ChannelService channelService,
            MessageService messageService) {
        System.out.println("\n[사용자 등록]");
        User user1 = new User("Amy", "amy@example.com");
        User user2 = new User("Chris", "chris@example.com");
        userService.create(user1);
        userService.create(user2);
        System.out.printf("사용자 등록 완료: %s (%s)%n", user1.getUsername(), user1.getEmail());
        System.out.printf("사용자 등록 완료: %s (%s)%n", user2.getUsername(), user2.getEmail());

        System.out.println("\n[사용자 전체 조회]");
        userService.readAll().forEach(user ->
                System.out.printf("  - %s (이메일: %s, 생성일: %s)%n",
                        user.getUsername(), user.getEmail(), Instant.ofEpochMilli(user.getCreatedAt()))
        );

        System.out.println("\n[사용자 수정]");
        user1.updateUsername("Amy Updated");
        userService.update(user1.getId(), user1);
        System.out.printf("수정된 사용자: %s (이메일: %s)%n", user1.getUsername(), user1.getEmail());

        System.out.println("\n[사용자 삭제]");
        userService.delete(user1.getId());
        System.out.println(userService.read(user1.getId()).isPresent() ?
                "삭제 실패!" : "삭제 성공!");

        System.out.println("\n[채널 등록]");
        Channel channel1 = new Channel("Second 채널", "업데이트된 채널 설명", user2);
        channelService.create(channel1);
        System.out.printf("채널 등록 완료: %s (설명: %s)%n", channel1.getName(), channel1.getDescription());

        System.out.println("\n[메시지 등록]");

        // 사용자 및 채널이 유효한지 검증 후 메시지 생성
        Optional<User> sender = userService.read(user2.getId());
        Optional<Channel> channel = channelService.read(channel1.getId());

        if (sender.isPresent() && channel.isPresent()) {
            Message message1 = new Message("안녕하세요!", sender.get().getId(), channel.get().getId());
            messageService.create(message1);
            System.out.printf("메시지 등록 완료: %s (보낸이: %s, 채널: %s)%n",
                    message1.getContent(), sender.get().getUsername(), channel.get().getName());
        } else {
            System.out.println("메시지 등록 실패: 유효하지 않은 사용자 또는 채널입니다.");
        }

        System.out.println("\n[메시지 삭제]");
        messageService.delete(channel1.getId());
        System.out.println(messageService.read(channel1.getId()).isPresent() ?
                "메시지 삭제 실패!" : "메시지 삭제 성공!");
    }
}
