package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class DiscodeitApplication {

    static UserResponse setupUser(UserService userService) {
        return userService.createUser(new UserRequest("woody34", "woody34@codeit.com", "woody1234", null));
    }

    static ChannelResponse setupChannel(ChannelService channelService) {
        return channelService.createPublicChannel(new ChannelRequest.CreatePublic("공지", "공지 채널입니다."));
    }

    static ChannelResponse setupPrivateChannel(ChannelService channelService, List<UUID> joinUsers) {
        return channelService.createPrivateChannel(new ChannelRequest.CreatePrivate(joinUsers));
    }

    static void messageCreateTest(MessageService messageService, UUID channelId, UUID authorId) {
        Message message = messageService.createMessage(new MessageRequest.Create("안녕하세요.", channelId, authorId, null));
        log.info("메시지 생성: {}", message.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 초기화
        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        UserResponse user = setupUser(userService);
        ChannelResponse channel = setupChannel(channelService);
        ChannelResponse privateChannel = setupPrivateChannel(channelService, List.of(user.id()));
        // 테스트
        messageCreateTest(messageService, channel.id(), user.id());
    }
}
