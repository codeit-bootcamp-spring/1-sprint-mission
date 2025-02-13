package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // UserRepository Bean 등록
    @Bean
    // matchIfMissing : 설정이 없거나
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserRepository userRepositoryJcf() {
        return new JCFUserRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserRepository userRepositoryFile() {
        return new FileUserRepository();
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
        return new FileChannelRepository();
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
        return new FileMessageRepository();
    }

    // ReadStatusRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ReadStatusRepository readStatusRepositoryJcf(){
        return new JCFReadStatusRepository();
    }
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public ReadStatusRepository readStatusRepositoryFile(){
        return new FileReadStatusRepository();
    }

    //UserStatusRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserStatusRepository userStatusRepositoryJcf(){
        return new JCFUserStatusRepository();
    }
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserStatusRepository userStatusRepositoryFile(){
        return new FileUserStatusRepository();
    }
    
    //BinaryContentRepository Bean 등록
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public BinaryContentRepository BinaryContentRepositoryJcf(){
        return new JCFBinaryContentRepository();
    }
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public BinaryContentRepository BinaryContentRepositoryFile(){
        return new FileBinaryContentRepository();
    }

    // ChaneUserRepository Bean 등록
    // 아 헉 내가 file 밖에 구현을 안했구나
    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
    public ChannelUserRepository channelUserRepositoryJcf(){
        return new FileChannelUserRepository();
    }

}
