package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import org.springframework.context.annotation.Bean;

//@Configuration
//@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFRepositoryConfig {

    @Bean
    public JCFBinaryContentRepository jcfBinaryContentRepository() {
        return new JCFBinaryContentRepository();
    }

    @Bean
    public JCFChannelRepository jcfChannelRepository() {
        return new JCFChannelRepository();
    }

    @Bean
    public JCFMessageRepository jcfMessageRepository() {
        return new JCFMessageRepository();
    }

    @Bean
    public JCFReadStatusRepository jcfReadStatusRepository() {
        return new JCFReadStatusRepository();
    }

    @Bean
    public JCFUserRepository jcfUserRepository() {
        return new JCFUserRepository();
    }

    @Bean
    public JCFUserStatusRepository jcfUserStatusRepository() {
        return new JCFUserStatusRepository();
    }
}
