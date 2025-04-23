package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BasicReadStatusServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(BasicReadStatusServiceTest.class);

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusMapper readStatusMapper;

  @InjectMocks
  private BasicReadStatusService readStatusService;

  private UUID readStatusId;
  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;
  private User user;
  private Channel channel;
  private ReadStatus readStatus;
  private ReadStatusDto readStatusDto;

  @BeforeEach
  void setUp() {
    readStatusId = UUID.randomUUID();
    userId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    lastReadAt = Instant.now();

    user = new User("testUser", "test@example.com", "password", null);
    ReflectionTestUtils.setField(user, "id", userId);

    channel = new Channel(ChannelType.PUBLIC, "testChannel", "test description");
    ReflectionTestUtils.setField(channel, "id", channelId);

    readStatus = new ReadStatus(user, channel, lastReadAt);
    ReflectionTestUtils.setField(readStatus, "id", readStatusId);

    readStatusDto = new ReadStatusDto(readStatusId, userId, channelId, lastReadAt);
  }

  @Test
  @Order(1)
  @DisplayName("읽음 상태 생성 성공")
  void createReadStatus_Success() {
    logger.info("==== 읽음 상태 생성 성공 테스트 시작 ====");

    // given
    ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId, lastReadAt);

    given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
    given(readStatusRepository.existsByUserIdAndChannelId(eq(userId), eq(channelId))).willReturn(
        false);
    given(readStatusRepository.save(any(ReadStatus.class))).willReturn(readStatus);
    given(readStatusMapper.toDto(any(ReadStatus.class))).willReturn(readStatusDto);

    // when
    ReadStatusDto result = readStatusService.create(request);

    // then
    assertThat(result).isEqualTo(readStatusDto);
    verify(readStatusRepository).save(any(ReadStatus.class));

    logger.info("==== 읽음 상태 생성 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(2)
  @DisplayName("존재하지 않는 사용자로 읽음 상태 생성 시 예외 발생")
  void createReadStatus_WithNonExistentUser_ThrowsException() {
    logger.info("==== 존재하지 않는 사용자로 읽음 상태 생성 시 예외 발생 테스트 시작 ====");

    // given
    ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId, lastReadAt);
    given(userRepository.findById(eq(userId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> readStatusService.create(request))
        .isInstanceOf(UserNotFoundException.class);

    logger.info("==== 존재하지 않는 사용자로 읽음 상태 생성 시 예외 발생 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(3)
  @DisplayName("존재하지 않는 채널로 읽음 상태 생성 시 예외 발생")
  void createReadStatus_WithNonExistentChannel_ThrowsException() {
    logger.info("==== 존재하지 않는 채널로 읽음 상태 생성 시 예외 발생 테스트 시작 ====");

    // given
    ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId, lastReadAt);
    given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> readStatusService.create(request))
        .isInstanceOf(ChannelNotFoundException.class);

    logger.info("==== 존재하지 않는 채널로 읽음 상태 생성 시 예외 발생 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(4)
  @DisplayName("이미 존재하는 읽음 상태 생성 시 예외 발생")
  void createReadStatus_WithExistingReadStatus_ThrowsException() {
    logger.info("==== 이미 존재하는 읽음 상태 생성 시 예외 발생 테스트 시작 ====");

    // given
    ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId, lastReadAt);
    given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
    given(readStatusRepository.existsByUserIdAndChannelId(eq(userId), eq(channelId))).willReturn(
        true);

    // when & then
    assertThatThrownBy(() -> readStatusService.create(request))
        .isInstanceOf(ReadStatusAlreadyExistsException.class);

    logger.info("==== 이미 존재하는 읽음 상태 생성 시 예외 발생 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(5)
  @DisplayName("읽음 상태 조회 성공")
  void findReadStatus_Success() {
    logger.info("==== 읽음 상태 조회 성공 테스트 시작 ====");

    // given
    given(readStatusRepository.findById(eq(readStatusId))).willReturn(Optional.of(readStatus));
    given(readStatusMapper.toDto(eq(readStatus))).willReturn(readStatusDto);

    // when
    ReadStatusDto result = readStatusService.find(readStatusId);

    // then
    assertThat(result).isEqualTo(readStatusDto);

    logger.info("==== 읽음 상태 조회 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(6)
  @DisplayName("존재하지 않는 읽음 상태 조회 시 예외 발생")
  void findReadStatus_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 읽음 상태 조회 시 예외 발생 테스트 시작 ====");

    // given
    given(readStatusRepository.findById(eq(readStatusId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> readStatusService.find(readStatusId))
        .isInstanceOf(ReadStatusNotFoundException.class);

    logger.info("==== 존재하지 않는 읽음 상태 조회 시 예외 발생 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(7)
  @DisplayName("사용자 ID로 읽음 상태 목록 조회 성공")
  void findAllByUserId_Success() {
    logger.info("==== 사용자 ID로 읽음 상태 목록 조회 성공 테스트 시작 ====");

    // given
    List<ReadStatus> readStatuses = List.of(readStatus);
    given(readStatusRepository.findAllByUserId(eq(userId))).willReturn(readStatuses);
    given(readStatusMapper.toDto(any(ReadStatus.class))).willReturn(readStatusDto);

    // when
    List<ReadStatusDto> result = readStatusService.findAllByUserId(userId);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(readStatusDto);

    logger.info("==== 사용자 ID로 읽음 상태 목록 조회 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(8)
  @DisplayName("읽음 상태 업데이트 성공")
  void updateReadStatus_Success() {
    logger.info("==== 읽음 상태 업데이트 성공 테스트 시작 ====");

    // given
    Instant newLastReadAt = Instant.now().plusSeconds(3600);
    ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(newLastReadAt);

    given(readStatusRepository.findById(eq(readStatusId))).willReturn(Optional.of(readStatus));
    given(readStatusMapper.toDto(any(ReadStatus.class))).willReturn(readStatusDto);

    // when
    ReadStatusDto result = readStatusService.update(readStatusId, request);

    // then
    assertThat(result).isEqualTo(readStatusDto);
    verify(readStatusRepository).findById(readStatusId);

    logger.info("==== 읽음 상태 업데이트 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(9)
  @DisplayName("존재하지 않는 읽음 상태 업데이트 시 예외 발생")
  void updateReadStatus_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 읽음 상태 업데이트 시 예외 발생 테스트 시작 ====");

    // given
    Instant newLastReadAt = Instant.now().plusSeconds(3600);
    ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(newLastReadAt);

    given(readStatusRepository.findById(eq(readStatusId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> readStatusService.update(readStatusId, request))
        .isInstanceOf(ReadStatusNotFoundException.class);

    logger.info("==== 존재하지 않는 읽음 상태 업데이트 시 예외 발생 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(10)
  @DisplayName("읽음 상태 삭제 성공")
  void deleteReadStatus_Success() {
    logger.info("==== 읽음 상태 삭제 성공 테스트 시작 ====");

    // given
    given(readStatusRepository.existsById(eq(readStatusId))).willReturn(true);

    // when
    readStatusService.delete(readStatusId);

    // then
    verify(readStatusRepository).deleteById(readStatusId);

    logger.info("==== 읽음 상태 삭제 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(11)
  @DisplayName("존재하지 않는 읽음 상태 삭제 시 예외 발생")
  void deleteReadStatus_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 읽음 상태 삭제 시 예외 발생 테스트 시작 ====");

    // given
    given(readStatusRepository.existsById(eq(readStatusId))).willReturn(false);

    // when & then
    assertThatThrownBy(() -> readStatusService.delete(readStatusId))
        .isInstanceOf(ReadStatusNotFoundException.class);

    logger.info("==== 존재하지 않는 읽음 상태 삭제 시 예외 발생 테스트 종료 ====");
    System.out.println("\n");
  }
}