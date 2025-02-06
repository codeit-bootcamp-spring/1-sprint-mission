package com.sprint.mission.discodeit.repository.config;

import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReadStatusRepositoryConfig {
    @Bean
    public ReadStatusRepository readStatusRepository(
            @Value("${discodeit.repository.type}") String repositoryType
    ) {
        if (repositoryType.equals("jcf")) {
            return new JCFReadStatusRepository();
        }else{
            return new FileReadStatusRepository();
        }
    }
}
