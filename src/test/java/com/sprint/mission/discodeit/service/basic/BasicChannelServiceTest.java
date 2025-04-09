package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

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

  // 공개 채널 생성 성공 케이스
  @Test
  void testCreatePublicChannel_Success() {
    // given - 요청 객체 생성 및 저장될 채널, 기대 반환 DTO 구성
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("테스트 채널", "테스트 채널 입니다.");
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
    UUID channelId = UUID.randomUUID();
    ReflectionTestUtils.setField(channel, "id", channelId);

    ChannelDto expectedDto = new ChannelDto(channelId, ChannelType.PUBLIC, request.name(),
        request.description(), List.of(), null);

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

    // when
    ChannelDto result = channelService.create(request);

    // then - 반환값 및 의존성 호출 검증
    assertEquals(expectedDto, result);
    verify(channelRepository).save(any(Channel.class));
    verify(channelMapper).toDto(any(Channel.class));
  }

  // 공개 채널 생성 실패 케이스
  @Test
  void testCreatePublicChannel_Failure() {
    // given - 예외 발생하도록 설정
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("테스트 채널", "테스트 채널 입니다.");
    given(channelRepository.save(any(Channel.class))).willThrow(new RuntimeException("DB Error"));

    // when & then - 예외 발생 검증
    assertThrows(RuntimeException.class, () -> channelService.create(request));
    verify(channelMapper, never()).toDto(any(Channel.class));
  }

  //프라이빗 채널 생성 성공 테스트
  @Test
  void testCreatePrivateChannel_Success() {
    // given - 요청 및 채널, 유저 더미 데이터 생성
    List<UUID> participantIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    UUID channelId = UUID.randomUUID();
    ReflectionTestUtils.setField(channel, "id", channelId);

    User user1 = new User("user1", "user1@email.com", "pw", null);
    ReflectionTestUtils.setField(user1, "id", participantIds.get(0));
    User user2 = new User("user2", "user2@email.com", "pw", null);
    ReflectionTestUtils.setField(user2, "id", participantIds.get(1));

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(userRepository.findAllById(participantIds)).willReturn(List.of(user1, user2));
    given(channelMapper.toDto(any(Channel.class))).willReturn(
        new ChannelDto(channelId, ChannelType.PRIVATE, null, null, List.of(), null));

    // when
    ChannelDto result = channelService.create(request);

    // then
    assertEquals(channelId, result.id());
    verify(channelRepository).save(any(Channel.class));
    verify(userRepository).findAllById(participantIds);
    verify(readStatusRepository).saveAll(anyList());
    verify(channelMapper).toDto(any(Channel.class));
  }

  //프라이빗 채널 생성 실패 테스트 (유저 조회 실패)
  @Test
  void testCreatePrivateChannel_Failure_UserFetchFail() {
    // given - 유저 조회 시 예외 발생하도록 설정
    List<UUID> participantIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    UUID channelId = UUID.randomUUID();
    ReflectionTestUtils.setField(channel, "id", channelId);

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(userRepository.findAllById(participantIds)).willThrow(
        new RuntimeException("User load fail"));

    // when & then - 예외 검증
    assertThrows(RuntimeException.class, () -> channelService.create(request));
    verify(channelRepository).save(any(Channel.class));
    verify(userRepository).findAllById(participantIds);
    verify(readStatusRepository, never()).saveAll(anyList());
    verify(channelMapper, never()).toDto(any(Channel.class));
  }

  //공개 채널 수정 성공 케이스
  @Test
  void testUpdatePublicChannel_Success() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("테스트 채널", "테스트 채널 입니다.");
    Channel existingChannel = new Channel(ChannelType.PUBLIC, "oldName", "oldDescription");
    ReflectionTestUtils.setField(existingChannel, "id", channelId);

    ChannelDto expectedDto = new ChannelDto(channelId, ChannelType.PUBLIC, "newName",
        "newDescription that is long enough", List.of(), null);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(existingChannel));
    given(channelMapper.toDto(existingChannel)).willReturn(expectedDto);

    // when
    ChannelDto result = channelService.update(channelId, request);

    // then
    assertEquals("newName", result.name());
    assertEquals("newDescription that is long enough", result.description());
    verify(channelRepository).findById(channelId);
    verify(channelMapper).toDto(existingChannel);
  }

  //채널 수정 실패 테스트 - 채널 없음
  @Test
  void testUpdatePublicChannel_Failure_ChannelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription that is long enough");

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when & then
    assertThrows(ChannelNotFoundException.class, () -> channelService.update(channelId, request));
  }

  //채널 수정 실패 테스트 - 프라이빗 채널 수정 시도
  @Test
  void testUpdatePublicChannel_Failure_PrivateChannel() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription that is long enough");

    Channel privateChannel = new Channel(ChannelType.PRIVATE, "old", "desc");
    ReflectionTestUtils.setField(privateChannel, "id", channelId);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

    // when & then
    assertThrows(PrivateChannelUpdateNotAllowedException.class,
        () -> channelService.update(channelId, request));
  }

  //채널 삭제 성공 테스트
  @Test
  void testDeleteChannel_Success() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(true);

    // when
    channelService.delete(channelId);

    // then
    verify(messageRepository).deleteAllByChannelId(channelId);
    verify(readStatusRepository).deleteAllByChannelId(channelId);
    verify(channelRepository).deleteById(channelId);
  }

  //채널 삭제 실패 테스트 - 존재하지 않는 채널
  @Test
  void testDeleteChannel_Failure_NotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);

    // when & then
    assertThrows(ChannelNotFoundException.class, () -> channelService.delete(channelId));
    verify(messageRepository, never()).deleteAllByChannelId(channelId);
    verify(readStatusRepository, never()).deleteAllByChannelId(channelId);
    verify(channelRepository, never()).deleteById(channelId);
  }

  // 유저 ID로 채널 목록 조회 성공
  @Test
  void testFindAllByUserId_Success() {
    // given - 유저가 구독한 채널이 존재하고, 이를 통해 채널 정보를 반환할 수 있는 상황 구성
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "name", "desc");
    ReflectionTestUtils.setField(channel, "id", channelId);

    ReadStatus readStatus = mock(ReadStatus.class);
    given(readStatus.getChannel()).willReturn(channel);

    ChannelDto dto = new ChannelDto(channelId, ChannelType.PUBLIC, "name", "desc", List.of(), null);

    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
    given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList())).willReturn(
        List.of(channel));
    given(channelMapper.toDto(channel)).willReturn(dto);

    // when - 메서드 호출
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then - 채널 목록 반환 결과 검증 및 각 Repository, Mapper가 호출되었는지 검증
    assertEquals(1, result.size());
    assertEquals(dto, result.get(0));
    verify(readStatusRepository).findAllByUserId(userId);
    verify(channelRepository).findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList());
    verify(channelMapper).toDto(channel);
  }

  //채널 목록 조회 실패 테스트 - ReadStatus 예외 발생 시 흐름 중단
  @Test
  void testFindAllByUserId_Failure_ReadStatusError() {
    // given - ReadStatus 조회에서 예외 발생하도록 설정
    UUID userId = UUID.randomUUID();
    given(readStatusRepository.findAllByUserId(userId)).willThrow(new RuntimeException("DB Error"));

    // when & then - 예외 발생 여부 확인
    assertThrows(RuntimeException.class, () -> channelService.findAllByUserId(userId));

    // then - 예외 발생 후 다른 의존성이 호출되지 않았는지 검증
    verify(readStatusRepository).findAllByUserId(userId);
    verify(channelRepository, never()).findAllByTypeOrIdIn(any(), anyList());
    verify(channelMapper, never()).toDto(any());
  }
}
