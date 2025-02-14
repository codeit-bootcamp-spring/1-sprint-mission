package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.interfacepac.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    
    //UserRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserRepository userRepositoryJcf() {
        return new JCFUserRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "file")
    public UserRepository userRepositoryFile() {
        return new FileUserRepository();
    }

    //ChannelRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ChannelRepository channelRepositoryJcf() {
        return new JCFChannelRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "file")
    public ChannelRepository channelRepositoryFile() {
        return new FileChannelRepository();
    }

    //MessageRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "jcf", matchIfMissing = true)
    public MessageRepository messageRepositoryJcf() {
        return new JCFMessageRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "file")
    public MessageRepository messageRepositoryFile() {
        return new FileMessageRepository();
    }

    //UserStatusRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserStatusRepository userStatusRepositoryJcf() {
        return new JCFUserStatusRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "file")
    public UserStatusRepository userStatusRepositoryFile() {
        return new FileUserStatusRepository();
    }

    //BinaryContentRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "jcf", matchIfMissing = true)
    public BinaryContentRepository binaryContentRepositoryJcf() {
        return new JCFBinaryContentRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "file")
    public BinaryContentRepository binaryContentRepositoryFile() {
        return new FileBinaryContentRepository();
    }

    //ReadStatusRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ReadStatusRepository readStatusRepositoryJcf() {
        return new JCFReadStatusRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodiet.repository.type", havingValue = "file")
    public ReadStatusRepository readStatusRepositoryFile() {
        return new FileReadStatusRepository();
    }
}
