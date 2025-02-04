package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.channel.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.channel.dto.CreateNewChannelRequest;
import com.sprint.mission.discodeit.entity.message.dto.DirectMessageInfoResponse;
import com.sprint.mission.discodeit.entity.message.dto.SendDirectMessageRequest;
import com.sprint.mission.discodeit.entity.user.dto.RegisterUserRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserInfoResponse;
import com.sprint.mission.discodeit.repository.file.channel.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.message.FileDirectMessageRepository;
import com.sprint.mission.discodeit.repository.file.user.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.channel.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.message.directMessage.DirectMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.user.UserRepository;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.message.directMessage.DirectMessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
    private static UserService userService;
    private static ChannelService channelService;
    private static DirectMessageService directMessageService;

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = SpringApplication.run(DiscodeitApplication.class, args);
        userService = appContext.getBean(UserService.class);
        channelService = appContext.getBean(ChannelService.class);
        directMessageService = appContext.getBean(DirectMessageService.class);

        UserInfoResponse user = registerUser(userService);
        ChannelResponse channelResponse = registerChannel(channelService, user.uuid());
        UserInfoResponse destinationUser = registerUser(userService);
        DirectMessageInfoResponse sendDirectMessage = sendDirectMessage(directMessageService,user.uuid(), destinationUser.uuid());

        saveToFile(appContext);
    }

    private static UserInfoResponse registerUser(UserService userService) {
        var registerRequest = new RegisterUserRequest("홍길동");
        return userService.register(registerRequest);
    }

    private static ChannelResponse registerChannel(ChannelService channelService, UUID userId) {
        var channelCreateRequest = new CreateNewChannelRequest(userId, "스프링부트_1기");
        return channelService.createChannelOrThrow(channelCreateRequest);
    }

    private static DirectMessageInfoResponse sendDirectMessage(DirectMessageService directMessageService, UUID sendUserId, UUID receiveUserId) {
        var sendDirectMessageRequest = new SendDirectMessageRequest(sendUserId, receiveUserId, "안녕하세요");
        return directMessageService.sendMessage(sendDirectMessageRequest);
    }

    private static void saveToFile(ConfigurableApplicationContext appContext) {
        saveChannelToFile(appContext);
        saveUserToFile(appContext);
        saveDirectMessageToFile(appContext);
    }

    private static void saveUserToFile(ConfigurableApplicationContext appContext) {
        FileUserRepository fileUserRepository = (FileUserRepository) appContext.getBean(UserRepository.class);
        fileUserRepository.saveToFile();
    }

    private static void saveChannelToFile(ConfigurableApplicationContext appContext) {
        FileChannelRepository fileChannelRepository = (FileChannelRepository) appContext.getBean(ChannelRepository.class);
        fileChannelRepository.saveToFile();
    }

    private static void saveDirectMessageToFile(ConfigurableApplicationContext appContext) {
        FileDirectMessageRepository fileDirectMessageRepository =
                (FileDirectMessageRepository) appContext.getBean(DirectMessageRepository.class);
        fileDirectMessageRepository.saveToFile();
    }
}
