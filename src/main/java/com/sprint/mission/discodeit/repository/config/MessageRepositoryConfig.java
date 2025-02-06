package com.sprint.mission.discodeit.repository.config;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageRepositoryConfig {
    @Bean
    public MessageRepository messageRepository(
            @Value("${discodeit.repository.type}") String repositoryType
    ) {
        if (repositoryType.equals("jcf")) {
            return new JCFMessageRepository();
        } else {
            return new FileMessageRepository();
        }
    }
}
