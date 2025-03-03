package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public BinaryContentRepository fileBinaryContentRepository() {
    return new FileBinaryContentRepository(".discodeit");
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public ChannelRepository fileChannelRepository() {
    return new FileChannelRepository(".discodeit");
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public MessageRepository fileMessageRepository() {
    return new FileMessageRepository(".discodeit");
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public ReadStatusRepository fileReadStatusRepository() {
    return new FileReadStatusRepository(".discodeit");
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public UserRepository fileUserRepository() {
    return new FileUserRepository(".discodeit");
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public UserStatusRepository fileUserStatusRepository() {
    return new FileUserStatusRepository(".discodeit");
  }


  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public BinaryContentRepository jcfBinaryContentRepository() {
    return new JCFBinaryContentRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public ChannelRepository jcfChannelRepository() {
    return new JCFChannelRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public MessageRepository jcfMessageRepository() {
    return new JCFMessageRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public ReadStatusRepository jcfReadStatusRepository() {
    return new JCFReadStatusRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public UserRepository jcfUserRepository() {
    return new JCFUserRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public UserStatusRepository jcfUserStatusRepository() {
    return new JCFUserStatusRepository();
  }
}
