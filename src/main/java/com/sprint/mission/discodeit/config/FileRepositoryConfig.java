package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileRepositoryConfig {

    @Value("${discordit.repository.file-directory}")
    private String fileDirectory;

    @Bean
    public UserRepository userRepository() {
        return new FileUserRepository(fileDirectory);
    }

    @Bean
    public ChannelRepository channelRepository() {
        return new FileChannelRepository(fileDirectory);
    }

    @Bean
    public MessageRepository messageRepository() {
        return new FileMessageRepository(fileDirectory);
    }

    @Bean
    public UserStatusRepository userStatusRepository() {
        return new FileUserStatusRepository(fileDirectory);
    }

    @Bean
    public ReadStatusRepository readStatusRepository() {
        return new FileReadStatusRepository(fileDirectory);
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        return new FileBinaryContentRepository(fileDirectory);
    }
}