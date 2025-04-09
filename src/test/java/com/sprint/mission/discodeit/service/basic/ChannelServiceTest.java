package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.testutil.TestFactory.createChannel;
import static com.sprint.mission.discodeit.testutil.TestFactory.createReadStatus;
import static com.sprint.mission.discodeit.testutil.TestFactory.createUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.Type;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

  @InjectMocks
  private ChannelService channelService;

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

  @Test
  void 공개_채널_생성_성공() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("테스트 채널", "설명");
    Channel channel = new Channel(Type.PUBLIC, "테스트 채널", "설명");
    ChannelDto dto = new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(),
        null, null);

    when(channelRepository.save(any(Channel.class))).thenReturn(channel);
    when(channelMapper.toDto(channel)).thenReturn(dto);

    ChannelDto result = channelService.create(request);

    assertEquals(dto, result);
  }

  @Test
  void 공개_채널_생성_시_예외_발생() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("에러채널", "설명");

    when(channelRepository.save(any())).thenThrow(new RuntimeException("DB 오류"));

    assertThrows(RuntimeException.class, () -> channelService.create(request));
  }

  @Test
  void 비공개_채널_생성_성공() {
    UUID userId = UUID.randomUUID();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(userId));
    Channel channel = new Channel(Type.PRIVATE, null, null);
    User user = new User("유저", "test@email.com", "password", null);
    ChannelDto dto = new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(),
        null, null);

    when(userRepository.findAllById(List.of(userId))).thenReturn(List.of(user));
    when(channelRepository.save(any(Channel.class))).thenReturn(channel);
    when(readStatusRepository.saveAll(any())).thenReturn(List.of());
    when(channelMapper.toDto(channel)).thenReturn(dto);

    ChannelDto result = channelService.create(request);

    assertEquals(dto, result);
  }

  @Test
  void 비공개_채널_생성_시_유저_없음() {
    UUID userId = UUID.randomUUID();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(userId));
    Channel channel = new Channel(Type.PRIVATE, null, null);

    when(userRepository.findAllById(List.of(userId))).thenReturn(List.of());
    when(channelRepository.save(any(Channel.class))).thenReturn(channel);

    ChannelDto dto = new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(),
        null, null);
    when(channelMapper.toDto(channel)).thenReturn(dto);

    // 유저가 없을 경우에도 에러를 발생시키지 않는 현재 코드 기준
    ChannelDto result = channelService.create(request);

    assertEquals(dto, result);
  }

  @Test
  void 공개_채널_업데이트_성공() {
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(Type.PUBLIC, "old", "desc");
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("new", "newDesc");
    ChannelDto dto = new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(),
        null, null);

    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
    when(channelMapper.toDto(channel)).thenReturn(dto);

    ChannelDto result = channelService.update(channelId, request);

    assertEquals(dto, result);
  }

  @Test
  void 비공개_채널_업데이트_실패() {
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(Type.PRIVATE, null, null);
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("new", "desc");

    when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

    assertThrows(PrivateChannelUpdateException.class,
        () -> channelService.update(channelId, request));
  }

  @Test
  void 채널_삭제_성공() {
    UUID channelId = UUID.randomUUID();

    when(channelRepository.existsById(channelId)).thenReturn(true);

    channelService.delete(channelId);

    verify(messageRepository).deleteAllByChannelId(channelId);
    verify(readStatusRepository).deleteAllByChannelId(channelId);
    verify(channelRepository).deleteById(channelId);
  }

  @Test
  void 존재하지_않는_채널_삭제_실패() {
    UUID channelId = UUID.randomUUID();

    when(channelRepository.existsById(channelId)).thenReturn(false);

    assertThrows(ChannelNotFoundException.class, () -> channelService.delete(channelId));
  }

  @Test
  void 유저가_구독한_채널_조회_성공() {
    UUID userId = UUID.randomUUID();
    Channel channel1 = createChannel(Type.PUBLIC, "공개1", "설명");
    Channel channel2 = createChannel(Type.PUBLIC, "공개2", "설명");
    UUID channelId1 = UUID.randomUUID();
    ReflectionTestUtils.setField(channel1, "id", channelId1);

    User user = createUser(userId, "사용자");
    ReadStatus readStatus = createReadStatus(user, channel2, Instant.now());

    when(readStatusRepository.findAllByUserId(userId)).thenReturn(List.of(readStatus));
    when(channelRepository.findAllByTypeOrIdIn(eq(Type.PUBLIC), any())).thenReturn(
        List.of(channel1, channel2));
    when(channelMapper.toDto(any(Channel.class))).thenReturn(
        new ChannelDto(UUID.randomUUID(), Type.PUBLIC, "채널명", "설명", null, null));

    List<ChannelDto> result = channelService.findAllByUserId(userId);

    assertEquals(2, result.size());
  }


}