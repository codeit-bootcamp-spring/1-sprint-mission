package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.*;
import com.sprint.mission.discodeit.exception.user.*;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.lang.reflect.Field;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private ChannelMapper channelMapper;
  @InjectMocks
  private BasicChannelService basicChannelService;

  private Field getFieldRecursively(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    while (clazz != null) {
      try {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName + " not found");
  }

  private void setId(Object target, UUID id) throws Exception {
    Field field = getFieldRecursively(target.getClass(), "id");
    field.set(target, id);
  }

  @Test
  void 공개채널_생성() throws Exception {
    PublicChannelCreateRequestDto request = new PublicChannelCreateRequestDto("공개 채널", "공개 채널 테스트");
    Channel channel = new Channel(ChannelType.PUBLIC, request.getName(), request.getDescription());
    setId(channel, UUID.randomUUID());

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(new ChannelDto());

    ChannelDto result = basicChannelService.createPublicChannel(request);

    assertNotNull(result);
    verify(channelRepository).save(any(Channel.class));
  }

  @Test
  void 비공개채널_생성() throws Exception {
    UUID user1Id = UUID.randomUUID(), user2Id = UUID.randomUUID();
    List<UUID> userIds = List.of(user1Id, user2Id);
    PrivateChannelCreateRequestDto request = new PrivateChannelCreateRequestDto(userIds);

    User user1 = new User("user1", "user1@naver.com", "1234", null);
    User user2 = new User("user2", "user2@naver.com", "1234", null);
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    setId(channel, UUID.randomUUID());

    given(userRepository.findAllById(userIds)).willReturn(List.of(user1, user2));
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(new ChannelDto());

    ChannelDto result = basicChannelService.createPrivateChannel(request);

    assertNotNull(result);
    verify(channelRepository).save(any(Channel.class));
    verify(readStatusRepository, times(2)).save(any(ReadStatus.class));
  }

  @Test
  void 비공개채널_생성_예외_유저누락() {
    UUID user1Id = UUID.randomUUID(), user2Id = UUID.randomUUID();
    List<UUID> users = List.of(user1Id, user2Id);
    PrivateChannelCreateRequestDto request = new PrivateChannelCreateRequestDto(users);

    given(userRepository.findAllById(users)).willReturn(
        List.of(new User("테스터", "user1@naver.com", "1234", null)));

    assertThrows(UserNotFoundException.class,
        () -> basicChannelService.createPrivateChannel(request));
  }

  @Test
  void 사용자ID로_채널목록조회() {
    UUID userId = UUID.randomUUID();
    Channel ch1 = new Channel(ChannelType.PRIVATE, "채널1", null);
    Channel ch2 = new Channel(ChannelType.PUBLIC, "채널2", null);

    ChannelDto dto1 = new ChannelDto();
    dto1.setName("채널1");
    ChannelDto dto2 = new ChannelDto();
    dto2.setName("채널2");

    given(channelRepository.findAllByUserId(userId)).willReturn(List.of(ch1, ch2));
    given(channelMapper.toDto(ch1)).willReturn(dto1);
    given(channelMapper.toDto(ch2)).willReturn(dto2);

    List<ChannelDto> result = basicChannelService.findAllByUserId(userId);

    assertEquals(2, result.size());
    assertEquals("채널1", result.get(0).getName());
    assertEquals("채널2", result.get(1).getName());
  }

  @Test
  void 채널수정_성공() {
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "old", "oldDesc");
    ChannelUpdateRequestDto request = new ChannelUpdateRequestDto("newName", "newDesc");

    ChannelDto dto = new ChannelDto();
    dto.setName("newName");
    dto.setDescription("newDesc");
    dto.setType(ChannelType.PUBLIC);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelMapper.toDto(channel)).willReturn(dto);

    ChannelDto result = basicChannelService.updateChannel(channelId, request);

    assertNotNull(result);
    assertEquals("newName", channel.getName());
    assertEquals("newDesc", channel.getDescription());
  }

  @Test
  void 채널수정_실패_채널없음() {
    UUID id = UUID.randomUUID();
    given(channelRepository.findById(id)).willReturn(Optional.empty());

    assertThrows(ChannelNotFoundException.class, () ->
        basicChannelService.updateChannel(id, new ChannelUpdateRequestDto("a", "b")));
  }

  @Test
  void 채널수정_실패_PRIVATE() {
    UUID id = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);

    given(channelRepository.findById(id)).willReturn(Optional.of(channel));

    assertThrows(PrivateChannelUpdateException.class, () ->
        basicChannelService.updateChannel(id, new ChannelUpdateRequestDto("a", "b")));

    then(channelMapper).shouldHaveNoInteractions();
  }

  @Test
  void 채널삭제_성공() {
    UUID id = UUID.randomUUID();
    given(channelRepository.existsById(id)).willReturn(true);

    basicChannelService.deleteChannel(id);

    verify(channelRepository).existsById(id);
    verify(channelRepository).deleteById(id);
  }

  @Test
  void 채널삭제_실패_예외() {
    UUID id = UUID.randomUUID();
    given(channelRepository.existsById(id)).willReturn(false);

    assertThrows(ChannelNotFoundException.class, () -> basicChannelService.deleteChannel(id));
  }
}
