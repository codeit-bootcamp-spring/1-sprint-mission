package com.sprint.mission.discodeit.config;


import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${discodeit.repository.type}")
    private String repositoryType;

    @Value("${discodeit.repository.file.path}")
    private String storagePath;

    @Bean
    public UserRepository userRepository() {
        if("jcf".equalsIgnoreCase(repositoryType)){
            return new JCFUserRepository();
        }else{
            return new FileUserRepository(storagePath);
        }
    }

    @Bean
    public UserStatusRepository userStatusRepository(){
        if("jcf".equalsIgnoreCase(repositoryType)){
            return new JCFUserStatusRepository();
        }else{
            return new FileUserStatusRepository(storagePath);
        }
    }

    @Bean
    public ReadStatusRepository readStatusRepository(){
        if("jcf".equalsIgnoreCase(repositoryType)){
            return new JCFReadStatusRepository();
        }else{
            return new FileReadStatusRepository(storagePath);
        }
    }

    @Bean
    public ChannelRepository channelRepository(){
        if("jcf".equalsIgnoreCase(repositoryType)){
            return new JCFChannelRepository();
        }else{
            return new FileChannelRepository(storagePath);
        }
    }

    @Bean
    public MessageRepository messageRepository(){
        if("jcf".equalsIgnoreCase(repositoryType)){
            return new JCFMessageRepository();
        }else{
            return new FileMessageRepository(storagePath);
        }
    }

    @Bean
    BinaryContentRepository binaryContentRepository(){
        if("jcf".equalsIgnoreCase(repositoryType)){
            return new JCFBinaryContentRepository();
        }else{
            return new FileBinaryContentRepository(storagePath);
        }
    }


}
