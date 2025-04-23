package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
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
class BasicChannelServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(BasicChannelServiceTest.class);

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService channelService;

  private UUID channelId;
  private UUID userId;
  private String channelName;
  private String channelDescription;
  private Channel channel;
  private ChannelDto channelDto;
  private User user;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    userId = UUID.randomUUID();
    channelName = "testChannel";
    channelDescription = "testDescription";

    channel = new Channel(ChannelType.PUBLIC, channelName, channelDescription);
    ReflectionTestUtils.setField(channel, "id", channelId);
    channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, channelName, channelDescription,
        List.of(), Instant.now());
    user = new User("testUser", "test@example.com", "password", null);
  }

  @Test
  @Order(1)
  @DisplayName("공개 채널 생성 성공")
  void createPublicChannel_Success() {
    logger.info("==== 공개 채널 생성 성공 테스트 시작 ====");

    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(channelName,
        channelDescription);
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    ChannelDto result = channelService.create(request);

    // then
    assertThat(result).isEqualTo(channelDto);
    verify(channelRepository).save(any(Channel.class));

    logger.info("==== 공개 채널 생성 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(2)
  @DisplayName("비공개 채널 생성 성공")
  void createPrivateChannel_Success() {
    logger.info("==== 비공개 채널 생성 성공 테스트 시작 ====");

    // given
    List<UUID> participantIds = List.of(userId);
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);
    given(userRepository.findAllById(eq(participantIds))).willReturn(List.of(user));
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    ChannelDto result = channelService.create(request);

    // then
    assertThat(result).isEqualTo(channelDto);
    verify(channelRepository).save(any(Channel.class));
    verify(readStatusRepository).<ReadStatus>saveAll(anyList());

    logger.info("==== 비공개 채널 생성 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(3)
  @DisplayName("채널 조회 성공")
  void findChannel_Success() {
    logger.info("==== 채널 조회 성공 테스트 시작 ====");

    // given
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    ChannelDto result = channelService.find(channelId);

    // then
    assertThat(result).isEqualTo(channelDto);

    logger.info("==== 채널 조회 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(4)
  @DisplayName("존재하지 않는 채널 조회 시 실패")
  void findChannel_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 채널 조회 시 실패 테스트 시작 ====");

    // given
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> channelService.find(channelId))
        .isInstanceOf(ChannelNotFoundException.class);

    logger.info("==== 존재하지 않는 채널 조회 시 실패 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(5)
  @DisplayName("사용자별 채널 목록 조회 성공")
  void findAllByUserId_Success() {
    logger.info("==== 사용자별 채널 목록 조회 성공 테스트 시작 ====");

    // given
    List<ReadStatus> readStatuses = List.of(new ReadStatus(user, channel, Instant.now()));
    given(readStatusRepository.findAllByUserId(eq(userId))).willReturn(readStatuses);
    given(
        channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), eq(List.of(channel.getId()))))
        .willReturn(List.of(channel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).containsExactly(channelDto);

    logger.info("==== 사용자별 채널 목록 조회 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(6)
  @DisplayName("공개 채널 수정 성공")
  void updatePublicChannel_Success() {
    logger.info("==== 공개 채널 수정 성공 테스트 시작 ====");

    // given
    String newName = "newChannelName";
    String newDescription = "newDescription";
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(newName, newDescription);

    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    ChannelDto result = channelService.update(channelId, request);

    // then
    assertThat(result).isEqualTo(channelDto);

    logger.info("==== 공개 채널 수정 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(7)
  @DisplayName("비공개 채널 수정 시도 시 실패")
  void updatePrivateChannel_ThrowsException() {
    logger.info("==== 비공개 채널 수정 시도 시 실패 테스트 시작 ====");

    // given
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription");
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(privateChannel));

    // when & then
    assertThatThrownBy(() -> channelService.update(channelId, request))
        .isInstanceOf(PrivateChannelUpdateException.class);

    logger.info("==== 비공개 채널 수정 시도 시 실패 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(8)
  @DisplayName("존재하지 않는 채널 수정 시도 시 실패")
  void updateChannel_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 채널 수정 시도 시 실패 테스트 시작 ====");

    // given
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription");
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> channelService.update(channelId, request))
        .isInstanceOf(ChannelNotFoundException.class);

    logger.info("==== 존재하지 않는 채널 수정 시도 시 실패 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(9)
  @DisplayName("채널 삭제 성공")
  void deleteChannel_Success() {
    logger.info("==== 채널 삭제 성공 테스트 시작 ====");

    // given
    given(channelRepository.existsById(eq(channelId))).willReturn(true);

    // when
    channelService.delete(channelId);

    // then
    verify(messageRepository).deleteAllByChannelId(eq(channelId));
    verify(readStatusRepository).deleteAllByChannelId(eq(channelId));
    verify(channelRepository).deleteById(eq(channelId));

    logger.info("==== 채널 삭제 성공 테스트 종료 ====");
    System.out.println("\n");
  }

  @Test
  @Order(10)
  @DisplayName("존재하지 않는 채널 삭제 시도 시 실패")
  void deleteChannel_WithNonExistentId_ThrowsException() {
    logger.info("==== 존재하지 않는 채널 삭제 시도 시 실패 테스트 시작 ====");

    // given
    given(channelRepository.existsById(eq(channelId))).willReturn(false);

    // when & then
    assertThatThrownBy(() -> channelService.delete(channelId))
        .isInstanceOf(ChannelNotFoundException.class);

    logger.info("==== 존재하지 않는 채널 삭제 시도 시 실패 테스트 종료 ====");
    System.out.println("\n");
  }
}