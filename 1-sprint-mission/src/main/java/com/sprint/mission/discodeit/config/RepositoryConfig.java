package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.interfacepac.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

  @Value("${discodeit.repository.file-directory:.discodeit}")
  private String fileDirectory;

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  //UserRepository Bean 등록
  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public UserRepository userRepositoryJcf() {
    return new JCFUserRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public UserRepository userRepositoryFile(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      ObjectMapper objectMapper) {
    return new FileUserRepository(fileDirectory, objectMapper);
  }

  //ChannelRepository Bean 등록
  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public ChannelRepository channelRepositoryJcf() {
    return new JCFChannelRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public ChannelRepository channelRepositoryFile(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      ObjectMapper objectMapper) {
    return new FileChannelRepository(fileDirectory, objectMapper);
  }

  //MessageRepository Bean 등록
  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public MessageRepository messageRepositoryJcf() {
    return new JCFMessageRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public MessageRepository messageRepositoryFile(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      ObjectMapper objectMapper) {
    return new FileMessageRepository(fileDirectory, objectMapper);
  }

  //UserStatusRepository Bean 등록
  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public UserStatusRepository userStatusRepositoryJcf() {
    return new JCFUserStatusRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public UserStatusRepository userStatusRepositoryFile(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      ObjectMapper objectMapper) {
    return new FileUserStatusRepository(fileDirectory, objectMapper);
  }

  //BinaryContentRepository Bean 등록
  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public BinaryContentRepository binaryContentRepositoryJcf() {
    return new JCFBinaryContentRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public BinaryContentRepository binaryContentRepositoryFile(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      ObjectMapper objectMapper) {
    return new FileBinaryContentRepository(fileDirectory, objectMapper);
  }

  //ReadStatusRepository Bean 등록
  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
  public ReadStatusRepository readStatusRepositoryJcf() {
    return new JCFReadStatusRepository();
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
  public ReadStatusRepository readStatusRepositoryFile(
      @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory,
      ObjectMapper objectMapper) {
    return new FileReadStatusRepository(fileDirectory, objectMapper);
  }
}
