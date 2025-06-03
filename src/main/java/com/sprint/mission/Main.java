package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;
import java.util.Optional;
import java.util.function.Consumer;

public class Main {
    static JCFUser userService = new JCFUser();
    static JCFChannel channelService = new JCFChannel();
    static JCFMessage messageService = new JCFMessage(channelService);

    public static void main(String[] args) {
        userServiceTest();

        messageServiceTest();

        channelServiceTest();
    }

    private static void channelServiceTest() {
        // 채널 생성
        Channel channel1 = channelService.createChannel("KBS");
        Channel channel2 = channelService.createChannel("EBS");
        log("채널 등록1", channel1.getChannelName());
        log("채널 등록2", channel2.getChannelName());

        // 채널 단건 조회
        testOptional("채널 조회 단건", channelService.getChannel(channel1.getId()),
                c -> log("채널 이름", c.getChannelName()));

        // 채널 다건 조회
        logAll("채널 조회 다건", channelService.getChannels().values(),
                c -> log("채널 이름", c.getChannelName()));

        // 채널 수정
        channelService.updateChannel(channel1.getId(), "KBS Updated");
        log("채널 수정", "완료");
        testOptional("채널 수정 성공", channelService.getChannel(channel1.getId()),
                c -> log("채널 이름", c.getChannelName()));

        // 채널 삭제
        testOptional("채널 삭제 성공", channelService.deleteChannel(channel1.getId()),
                c -> log("채널 이름", c.getChannelName()));

        // 삭제 여부 확인
        logAll("채널 조회 다건", channelService.getChannels().values(),
                c -> log("채널 이름", c.getChannelName()));
    }

    private static void messageServiceTest() {
        // 유저 생성
        User user1 = userService.createUser("1");
        User user2 = userService.createUser("2");

        // 채널 생성
        Channel channel1 = channelService.createChannel("KBS");
        Channel channel2 = channelService.createChannel("EBS");

        // 메시지 생성
        Message message1 = messageService.createMessage(user1.getId(), channel1.getId(), "Hello");
        Message message2 = messageService.createMessage(user2.getId(), channel2.getId(), "World");
        log("메시지 등록1", message1.getText());
        log("메시지 등록2", message2.getText());

        // 메시지 단건 조회
        testOptional("메시지 조회 단건", messageService.getMessage(message1.getId()),
                m -> log("메시지 내용", m.getText()));

        // 메시지 다건 조회
        logAll("메시지 조회 다건", messageService.getMessages().values(),
                m -> log("메시지 내용", m.getText()));

        // 메시지 수정
        messageService.updateMessage(message1.getId(), "Hello Updated");
        log("메시지 수정", "완료");
        testOptional("메시지 수정 성공", messageService.getMessage(message1.getId()),
                m -> log("메시지 내용", m.getText()));

        // 메시지 삭제
        testOptional("메시지 삭제 성공", messageService.deleteMessage(message1.getId()),
                m -> log("메시지 내용", m.getText()));

        // 삭제 여부 확인
        logAll("메시지 조회 다건", messageService.getMessages().values(),
                m -> log("메시지 내용", m.getText()));
    }

    private static void userServiceTest() {
        // 유저 생성
        User user1 = userService.createUser("1");
        User user2 = userService.createUser("2");
        log("유저 등록1", user1.getUsername());
        log("유저 등록2", user2.getUsername());

        // 유저 단건 조회
        testOptional("유저 조회 단건", userService.getUser(user1.getId()),
                u -> log("유저 이름", u.getUsername()));

        // 유저 다건 조회
        logAll("유저 조회 다건", userService.getUsers().values(),
                u -> log("유저 이름", u.getUsername()));

        // 유저 수정
        userService.updateUser(user1.getId(), "11");
        log("유저 수정", "완료");
        testOptional("유저 수정 성공", userService.getUser(user1.getId()),
                u -> log("유저 이름", u.getUsername()));

        // 유저 삭제
        testOptional("유저 삭제 성공", userService.deleteUser(user1.getId()),
                u -> log("유저 이름", u.getUsername()));

        // 삭제 여부 확인
        logAll("유저 조회 다건", userService.getUsers().values(),
                u -> log("유저 이름", u.getUsername()));
    }

    // 공통 로깅 메서드
    private static void log(String action, String detail) {
        System.out.println(action + ": " + detail);
    }

    // Optional 처리 유틸리티
    private static <T> void testOptional(String action, Optional<T> optional, Consumer<T> consumer) {
        optional.ifPresentOrElse(consumer, () -> log(action, "실패 (데이터 없음)"));
    }

    // 컬렉션 처리 유틸리티
    private static <T> void logAll(String action, Iterable<T> items, Consumer<T> consumer) {
        System.out.println(action + ":");
        items.forEach(consumer);
    }
}
