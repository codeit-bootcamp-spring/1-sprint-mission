package com.sprint.mission.discodeit.repository.config;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRepositoryConfig {

//    @Bean
//    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
//    public UserRepository fileUserRepository() {
//        return new FileUserRepository();
//    }
//
//    @Bean
//    @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
//    public UserRepository jcfUserRepository() {
//        return new JCFUserRepository();
//    }

    @Bean
    public UserRepository userRepository(
            @Value("${discodeit.repository.type}") String repositoryType
    ) {
        if (repositoryType.equals("jcf")) {
            return new JCFUserRepository();
        } else {
            return new FileUserRepository();
        }
    }
}
