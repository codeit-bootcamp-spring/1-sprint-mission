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
import com.sprint.mission.discodeit.repository.jcf.JCFUserReposiroty;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RepositoryConfig {

  @Value("${discodeit.repository.type}")
  private String repositoryType;

  @Bean
  @Primary
  public BinaryContentRepository binaryContentRepository(FileConfig fileConfig) {
    if ("file".equalsIgnoreCase(repositoryType)) {
      return new FileBinaryContentRepository(fileConfig);
    } else {
      return new JCFBinaryContentRepository();
    }
  }

  @Bean
  @Primary
  public ChannelRepository channelRepository(FileConfig fileConfig) {
    if ("file".equalsIgnoreCase(repositoryType)) {
      return new FileChannelRepository(fileConfig);
    } else {
      return new JCFChannelRepository();
    }
  }

  @Bean
  @Primary
  public MessageRepository messageRepository(FileConfig fileConfig) {
    if ("file".equalsIgnoreCase(repositoryType)) {
      return new FileMessageRepository(fileConfig);
    } else {
      return new JCFMessageRepository();
    }
  }

  @Bean
  @Primary
  public ReadStatusRepository readStatusRepository(FileConfig fileConfig) {
    if ("file".equalsIgnoreCase(repositoryType)) {
      return new FileReadStatusRepository(fileConfig);
    } else {
      return new JCFReadStatusRepository();
    }
  }

  @Bean
  @Primary
  public UserRepository userRepository(FileConfig fileConfig) {
    if ("file".equalsIgnoreCase(repositoryType)) {
      return new FileUserRepository(fileConfig);
    } else {
      return new JCFUserReposiroty();
    }
  }

  @Bean
  @Primary
  public UserStatusRepository userStatusRepository(FileConfig fileConfig) {
    if ("file".equalsIgnoreCase(repositoryType)) {
      return new FileUserStatusRepository(fileConfig);
    } else {
      return new JCFUserStatusRepository();
    }
  }
}
