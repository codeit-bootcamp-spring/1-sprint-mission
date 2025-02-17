package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final

    @Bean
    public UserStatusService userStatusService() {
        return new BasicUserStatusService(userStatusRepository, userRepository);
    }

    @Bean
    public ReadStatusService readStatusService() {
        return new BasicReadStatusService(readStatusRepository, channelRepository, userRepository);
    }

    @Bean
    public BinaryContentService binaryContentService() {
        return new BasicBinaryContentService(binaryContentRepository);
    }

    @Bean
    public UserService userService() {
        return new BasicUserService(userRepository, userStatusService(), binaryContentService());
    }

    @Bean
    public ChannelService channelService() {
        return new BasicChannelService(channelRepository, userRepository, messageRepository, readStatusService(), userStatusService());
    }

    @Bean
    public MessageService messageService() {
        return new BasicMessageService(messageRepository, userService(), channelService(), binaryContentService());
    }

}
