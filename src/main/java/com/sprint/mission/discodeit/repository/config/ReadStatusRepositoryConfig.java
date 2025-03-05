package com.sprint.mission.discodeit.repository.config;

import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReadStatusRepositoryConfig {
    @Bean
    public ReadStatusRepository readStatusRepository(
            @Value("${discodeit.repository.type}") String repositoryType,
            @Value("${discodeit.repository.file.storage-path}") String storagePath
    ) {
        if ("jcf".equalsIgnoreCase(repositoryType)) {
            return new JCFReadStatusRepository();
        }else {
            return new FileReadStatusRepository(storagePath);
        }
    }
}
