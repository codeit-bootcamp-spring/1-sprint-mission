package com.sprint.mission;

import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.file.main.FileChannelRepository;
import com.sprint.mission.repository.file.main.FileMessageRepository;
import com.sprint.mission.repository.file.main.FileUserRepository;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFMessageRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public UserRepository jcfUserRepository(){
        return new JCFUserRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public UserRepository fileUserRepository(){
        return new FileUserRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public ChannelRepository jcfChannelRepository(){
        return new JCFChannelRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public ChannelRepository fileChannelRepository(){
        return new FileChannelRepository();
    }


    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
    public MessageRepository jcfMessageRepository(){
        return new JCFMessageRepository();
    }

    @Bean
    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
    public MessageRepository fileMessageRepository(){
        return new FileMessageRepository();
    }



}
