package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.interfacepac.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepositoryConfig {

    @Value("${discodeit.repository.file-directory:.discodeit}")
    private String fileDirectory;

    // UserRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserRepository userRepositoryJcf() {
        return new JCFUserRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserRepository userRepositoryFile() {
        return new FileUserRepository(fileDirectory);
    }

    // ChannelRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ChannelRepository channelRepositoryJcf() {
        return new JCFChannelRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public ChannelRepository channelRepositoryFile() {
        return new FileChannelRepository(fileDirectory);
    }

    // MessageRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public MessageRepository messageRepositoryJcf() {
        return new JCFMessageRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public MessageRepository messageRepositoryFile() {
        return new FileMessageRepository(fileDirectory);
    }

    // UserStatusRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserStatusRepository userStatusRepositoryJcf() {
        return new JCFUserStatusRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserStatusRepository userStatusRepositoryFile() {
        return new FileUserStatusRepository(fileDirectory);
    }

    // BinaryContentRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public BinaryContentRepository binaryContentRepositoryJcf() {
        return new JCFBinaryContentRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public BinaryContentRepository binaryContentRepositoryFile() {
        return new FileBinaryContentRepository(fileDirectory);
    }

    // ReadStatusRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ReadStatusRepository readStatusRepositoryJcf() {
        return new JCFReadStatusRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public ReadStatusRepository readStatusRepositoryFile() {
        return new FileReadStatusRepository(fileDirectory);
    }
}

