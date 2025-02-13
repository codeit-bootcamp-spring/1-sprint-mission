package com.sprint.mission.discodeit.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FileRepositoryConfig.class, JCFRepositoryConfig.class, ServiceConfig.class})
public class AppConfig {

}
