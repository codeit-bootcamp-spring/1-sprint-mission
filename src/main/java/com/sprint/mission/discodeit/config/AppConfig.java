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

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

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
        return new BasicUserService(userRepository, userStatusService() ,binaryContentService());
    }
    //todo 순환참조 해결하기 - message <-> channel
    @Bean
    public MessageService messageService(){
        return new BasicMessageService(messageRepository, userService(), channelService(), binaryContentService());
    }

    @Bean
    public ChannelService channelService() {
        return new BasicChannelService(channelRepository, userRepository, messageRepository, readStatusService(), userStatusService());
    }

}
