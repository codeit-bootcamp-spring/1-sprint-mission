package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Value("${discodeit.repository.type:jcf}") // 기본값은 jcf
    private String repositoryType;

    @Bean
    public UserRepository userRepository() {
        return repositoryType.equalsIgnoreCase("file")
                ? new FileUserRepository()
                : new JCFUserRepository();
    }

    @Bean
    public ChannelRepository channelRepository() {
        return repositoryType.equalsIgnoreCase("file")
                ? new FileChannelRepository()
                : new JCFChannelRepository();
    }

    @Bean
    public MessageRepository messageRepository() {
        return repositoryType.equalsIgnoreCase("file")
                ? new FileMessageRepository()
                : new JCFMessageRepository();
    }

    @Bean
    public ReadStatusRepository readStatusRepository() {
        return repositoryType.equalsIgnoreCase("file")
                ? new FileReadStatusRepository()
                : new JCFReadStatusRepository();
    }

    @Bean
    public UserStatusRepository userStatusRepository() {
        return repositoryType.equalsIgnoreCase("file")
                ? new FileUserStatusRepository()
                : new JCFUserStatusRepository();
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        return repositoryType.equalsIgnoreCase("file")
                ? new FileBinaryContentRepository()
                : new JCFBinaryContentRepository();
    }
}
