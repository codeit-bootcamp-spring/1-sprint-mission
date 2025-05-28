package com.sprint.mission.discodeit.service.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReadStatusServiceTest {

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @InjectMocks
  private ReadStatusService readStatusService;

  @Test
  @DisplayName("읽음 상태 생성 - 성공")
  void createReadStatus() {
    UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID channelId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID readStatusId = UUID.fromString("33333333-3333-3333-3333-333333333333");
    Instant now = Instant.now();

    ReadStatusRequest request = new ReadStatusRequest(userId, channelId, now);

    User user = mock(User.class);
    when(user.getId()).thenReturn(userId);
    Channel channel = mock(Channel.class);
    when(channel.getId()).thenReturn(channelId);

    when(userRepository.getReferenceById(userId)).thenReturn(user);
    when(channelRepository.getReferenceById(channelId)).thenReturn(channel);
    when(readStatusRepository.findReadStatusByUser_IdAndChannel_Id(any(), any()))
        .thenReturn(Optional.empty());

    doAnswer(invocation -> {
      ReadStatus entity = invocation.getArgument(0);
      ReflectionTestUtils.setField(entity, "id", readStatusId);
      return entity;
    }).when(readStatusRepository).save(any(ReadStatus.class));

    ReadStatusDto result = readStatusService.create(request);

    assertNotNull(result);
    assertEquals(readStatusId, result.id());
    assertEquals(userId, result.userId());
    assertEquals(channelId, result.channelId());

    verify(userRepository).getReferenceById(userId);
    verify(channelRepository).getReferenceById(channelId);
    verify(readStatusRepository).findReadStatusByUser_IdAndChannel_Id(any(), any());
    verify(readStatusRepository).save(any(ReadStatus.class));
  }
  
  @Test
  @DisplayName("사용자 ID로 읽음 상태 조회 - 성공")
  void findByUserId() {
    UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID channelId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID readStatusId = UUID.fromString("33333333-3333-3333-3333-333333333333");
    Instant now = Instant.now();

    User user = mock(User.class);
    when(user.getId()).thenReturn(userId);

    Channel channel = mock(Channel.class);
    when(channel.getId()).thenReturn(channelId);

    ReadStatus readStatus = mock(ReadStatus.class);
    when(readStatus.getId()).thenReturn(readStatusId);
    when(readStatus.getUser()).thenReturn(user);
    when(readStatus.getChannel()).thenReturn(channel);
    when(readStatus.getLastReadAt()).thenReturn(now);

    when(readStatusRepository.findAllByUser_Id(userId)).thenReturn(List.of(readStatus));

    List<ReadStatusDto> results = readStatusService.findByUserId(userId);

    assertNotNull(results);
    assertEquals(1, results.size());
    assertEquals(readStatusId, results.get(0).id());
    assertEquals(userId, results.get(0).userId());
    assertEquals(channelId, results.get(0).channelId());

    verify(readStatusRepository).findAllByUser_Id(userId);
  }

  @Test
  @DisplayName("읽음 상태 조회 실패 - 없는 유저 ID")
  void findByUserIdFail() {
    UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

    when(readStatusRepository.findAllByUser_Id(userId)).thenReturn(Collections.emptyList());

    ReadStatusNotFoundException exception = assertThrows(
        ReadStatusNotFoundException.class,
        () -> readStatusService.findByUserId(userId)
    );

    assertEquals(ErrorCode.READ_STATUS_NOT_FOUND, exception.getErrorCode());
    assertTrue(exception.getDetails().containsKey(userId.toString()));

    verify(readStatusRepository).findAllByUser_Id(userId);
  }

  @Test
  @DisplayName("읽음 상태 업데이트 - 성공")
  void updateReadStatus() {
    UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    UUID channelId = UUID.fromString("22222222-2222-2222-2222-222222222222");
    UUID readStatusId = UUID.fromString("33333333-3333-3333-3333-333333333333");
    Instant now = Instant.now();
    Instant newTime = now.plusSeconds(3600);

    ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(newTime);

    User user = mock(User.class);
    when(user.getId()).thenReturn(userId);

    Channel channel = mock(Channel.class);
    when(channel.getId()).thenReturn(channelId);

    ReadStatus readStatus = mock(ReadStatus.class);
    when(readStatus.getId()).thenReturn(readStatusId);
    when(readStatus.getUser()).thenReturn(user);
    when(readStatus.getChannel()).thenReturn(channel);
    when(readStatus.getLastReadAt()).thenReturn(newTime);
    when(readStatus.changeLastReadAt(newTime)).thenReturn(readStatus);

    when(readStatusRepository.findById(readStatusId)).thenReturn(Optional.of(readStatus));

    ReadStatusDto result = readStatusService.update(readStatusId, request);

    assertNotNull(result);
    assertEquals(readStatusId, result.id());
    assertEquals(userId, result.userId());
    assertEquals(channelId, result.channelId());
    assertEquals(newTime, result.lastReadAt());

    verify(readStatusRepository).findById(readStatusId);
    verify(readStatus).changeLastReadAt(newTime);
  }

  @Test
  @DisplayName("존재하지 않는 읽음 상태 업데이트 - 실패")
  void updateNonExistingReadStatus() {
    UUID readStatusId = UUID.fromString("33333333-3333-3333-3333-333333333333");
    Instant now = Instant.now();
    Instant newTime = now.plusSeconds(3600);

    ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(newTime);

    when(readStatusRepository.findById(readStatusId)).thenReturn(Optional.empty());

    ReadStatusNotFoundException exception = assertThrows(
        ReadStatusNotFoundException.class,
        () -> readStatusService.update(readStatusId, request)
    );

    assertEquals(ErrorCode.READ_STATUS_NOT_FOUND, exception.getErrorCode());
    assertTrue(exception.getDetails().containsKey(readStatusId.toString()));

    verify(readStatusRepository).findById(readStatusId);
  }
}