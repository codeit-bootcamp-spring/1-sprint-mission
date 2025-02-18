package com.sprint.mission.discodeit.repository.config;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelRepositoryConfig {
    @Bean
    public ChannelRepository channelRepository(
            @Value("${discodeit.repository.type}") String repositoryType,
            @Value("${discodeit.repository.file.storage-path}") String storagePath
    ) {
        if ("jcf".equalsIgnoreCase(repositoryType)) {
            return new JCFChannelRepository();
        }else {
            return new FileChannelRepository(storagePath);
        }
    }
}
