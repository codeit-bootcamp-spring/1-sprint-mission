package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicTest {
    static UserDto setupUser(UserService userService) {
        return userService.createUser(new UserDto("woody","woodyPassword", "woody@naver.com"));
    }

    static Channel setupChannel(ChannelService channelService) {
        ChannelDto channelDto = channelService.createPublicChannel(new ChannelDto("공지", ChannelType.PUBLIC));
        return channelService.getChannel(channelDto.id());
    }

    static void messageCreateTest(MessageService messageService, Channel channel, UserDto author) {
        UUID messageId = messageService.createMessage(channel.getId(), "안녕");
        Message message = messageService.getMessage(messageId);
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        BasicFactory basicFactory = new BasicFactory();
        UserService userService = basicFactory.getUserService();
        ChannelService channelService = basicFactory.getChannelService();
        MessageService messageService = basicFactory.getMessageService();

        // 셋업
        UserDto userDto = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, userDto);
    }
}

