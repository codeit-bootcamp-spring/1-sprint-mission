package com.sprint.mission.discodeit.repository.config;

import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserStatusRepositoryConfig {
    @Bean
    public UserStatusRepository userStatusRepository(
            @Value("{discodeit.repository.type}" ) String repositorytype
    ) {
        if (repositorytype.equals("jcf")) {
            return new JCFUserStatusRepository();
        }else{
            return new FileUserStatusRepository();
        }
    }
}
