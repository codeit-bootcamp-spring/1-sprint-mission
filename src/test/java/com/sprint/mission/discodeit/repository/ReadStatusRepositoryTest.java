package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.entity.*;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestAuditingConfig.class)
public class ReadStatusRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(ReadStatusRepositoryTest.class);

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private ReadStatusRepository readStatusRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;

  private User createTestUser(String username, String email) {
    BinaryContent profile = new BinaryContent(username + "-profile.jpg", 1024L, "image/jpeg");
    binaryContentRepository.save(profile);
    User user = new User(username, email, "password123!@#", profile);
    userRepository.save(user);
    userStatusRepository.save(new UserStatus(user, Instant.now()));
    entityManager.flush();
    return user;
  }

  private Channel createTestChannel(ChannelType type, String name) {
    Channel channel = new Channel(type, name, "설명: " + name);
    channelRepository.save(channel);
    entityManager.flush();
    return channel;
  }

  private ReadStatus createTestReadStatus(User user, Channel channel) {
    ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
    readStatusRepository.save(readStatus);
    entityManager.flush();
    return readStatus;
  }

  @Test
  @Order(1)
  @DisplayName("읽음 상태를 저장하고 ID로 조회할 수 있다")
  void saveAndFindReadStatusById() {
    log.info("======== 읽음상태 저장 및 ID로 조회 테스트 시작 ========");

    User user = createTestUser("readuser", "read@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "read-channel");
    ReadStatus savedReadStatus = createTestReadStatus(user, channel);

    Optional<ReadStatus> found = readStatusRepository.findById(savedReadStatus.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    assertThat(found.get().getChannel().getId()).isEqualTo(channel.getId());

    log.info("======== 읽음상태 저장 및 ID로 조회 테스트 종료 ========");
  }

  @Test
  @Order(2)
  @DisplayName("사용자 ID로 모든 읽음 상태를 조회할 수 있다")
  void findAllReadStatusByUserId() {
    log.info("======== 사용자 ID로 읽음상태 목록 조회 테스트 시작 ========");

    User user = createTestUser("multiread", "multiread@example.com");
    Channel ch1 = createTestChannel(ChannelType.PUBLIC, "read-channel1");
    Channel ch2 = createTestChannel(ChannelType.PUBLIC, "read-channel2");

    createTestReadStatus(user, ch1);
    createTestReadStatus(user, ch2);

    List<ReadStatus> results = readStatusRepository.findAllByUserId(user.getId());

    assertThat(results).hasSize(2);
    assertThat(results).extracting("user.id").containsOnly(user.getId());
    assertThat(results).extracting("channel.id")
        .containsExactlyInAnyOrder(ch1.getId(), ch2.getId());

    log.info("======== 사용자 ID로 읽음상태 목록 조회 테스트 종료 ========");
  }

  @Test
  @Order(3)
  @DisplayName("존재하지 않는 사용자 ID로 조회 시 빈 리스트를 반환한다")
  void findByInvalidUserIdReturnsEmptyList() {
    log.info("======== 존재하지 않는 사용자 ID로 읽음상태 목록 조회 테스트 시작 ========");

    List<ReadStatus> result = readStatusRepository.findAllByUserId(UUID.randomUUID());

    assertThat(result).isEmpty();

    log.info("======== 존재하지 않는 사용자 ID로 읽음상태 목록 조회 테스트 종료 ========");
  }

  @Test
  @Order(4)
  @DisplayName("채널 ID로 읽음 상태를 조회하며 사용자 정보를 함께 불러올 수 있다")
  void findAllReadStatusByChannelWithUser() {
    log.info("======== 채널 ID와 함께 사용자 정보 조회 테스트 시작 ========");

    User user1 = createTestUser("readuser1", "read1@example.com");
    User user2 = createTestUser("readuser2", "read2@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "read-status-channel");

    createTestReadStatus(user1, channel);
    createTestReadStatus(user2, channel);

    List<ReadStatus> results = readStatusRepository.findAllByChannelIdWithUser(channel.getId());

    assertThat(results).hasSize(2);
    assertThat(results.get(0).getUser().getStatus()).isNotNull();
    assertThat(results.get(1).getUser().getStatus()).isNotNull();

    log.info("======== 채널 ID와 함께 사용자 정보 조회 테스트 종료 ========");
  }

  @Test
  @Order(5)
  @DisplayName("사용자와 채널 ID로 읽음 상태 존재 여부를 확인할 수 있다")
  void existsByUserIdAndChannelId() {
    log.info("======== 사용자와 채널 ID로 읽음상태 존재 여부 확인 테스트 시작 ========");

    User user = createTestUser("existsuser", "exists@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "exists-channel");
    createTestReadStatus(user, channel);

    boolean exists = readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId());
    boolean notExists = readStatusRepository.existsByUserIdAndChannelId(user.getId(),
        UUID.randomUUID());

    assertThat(exists).isTrue();
    assertThat(notExists).isFalse();

    log.info("======== 사용자와 채널 ID로 읽음상태 존재 여부 확인 테스트 종료 ========");
  }

  @Test
  @Order(6)
  @DisplayName("채널 ID로 모든 읽음 상태를 삭제할 수 있다")
  void deleteAllReadStatusByChannelId() {
    log.info("======== 채널 ID로 모든 읽음상태 삭제 테스트 시작 ========");

    User user1 = createTestUser("deleteread1", "deleteread1@example.com");
    User user2 = createTestUser("deleteread2", "deleteread2@example.com");
    Channel channel = createTestChannel(ChannelType.PUBLIC, "delete-read-channel");

    createTestReadStatus(user1, channel);
    createTestReadStatus(user2, channel);

    List<ReadStatus> before = readStatusRepository.findAllByChannelIdWithUser(channel.getId());
    assertThat(before).hasSize(2);

    readStatusRepository.deleteAllByChannelId(channel.getId());
    entityManager.flush();
    entityManager.clear();

    List<ReadStatus> after = readStatusRepository.findAllByChannelIdWithUser(channel.getId());
    assertThat(after).isEmpty();

    log.info("======== 채널 ID로 모든 읽음상태 삭제 테스트 종료 ========");
  }
}
