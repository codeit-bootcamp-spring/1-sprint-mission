package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicTest {
    static User setupUser(UserService userService) {
        UUID userId = userService.createUser("woody");
        return userService.getUser(userId);
    }

    static Channel setupChannel(ChannelService channelService) {
        UUID channelId = channelService.createChannel("공지");
        return channelService.getChannel(channelId);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        UUID messageId = messageService.createMessage(channel.getChannelId(), "안녕");
        Message message = messageService.getMessage(messageId);
        System.out.println("메시지 생성: " + message.getMessageId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        BasicFactory basicFactory = new BasicFactory();
        UserService userService = basicFactory.getUserService();
        ChannelService channelService = basicFactory.getChannelService();
        MessageService messageService = basicFactory.getMessageService();

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}

