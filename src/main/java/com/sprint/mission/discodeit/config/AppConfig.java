package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.basic.BasicUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {


    @Bean
    public UserService userService(UserRepository userRepository, BinaryContentRepository binaryContentRepository) {
        return new BasicUserService(userRepository, binaryContentRepository);
    }


}