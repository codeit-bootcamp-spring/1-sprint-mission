package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.IllegalChannelException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelCanNotModifyException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService channelService;


  @Test
  @DisplayName("공개 채널 생성")
  void createPublicChannel() throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("pubCh",
        "public channel");
    Channel channel = new Channel("pubCh", "public channel", ChannelType.PUBLIC);

    when(channelRepository.save(any(Channel.class)))
        .thenReturn(channel);

    ChannelDto savedChanel = channelService.createPublic(request);

    assertNotNull(savedChanel);
    assertEquals(channel.getType(), savedChanel.type());
    assertEquals(channel.getName(), savedChanel.name());

    verify(channelRepository).save(any(Channel.class));
  }

  @Test
  @DisplayName("공개 채널 생성 - 실패")
  void failCreatePublicChannel() throws Exception {
    // name과 description은 null이면 안됨
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(null,
        null);

    assertThrows(IllegalChannelException.class, () -> channelService.createPublic(request));

    verify(channelRepository, never()).save(any(Channel.class));
  }

  @Test
  @DisplayName("비공개 채널 생성")
  void createPrivateChannel() throws Exception {
    List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(ids);
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);

    when(channelRepository.save(any(Channel.class))).thenReturn(channel);

    ChannelDto savedChannel = channelService.createPrivate(request);

    assertNotNull(savedChannel);
    assertEquals(ChannelType.PRIVATE, savedChannel.type());

    verify(channelRepository).save(any(Channel.class));
  }

  @Test
  @DisplayName("비공개 채널 생성 - 실패")
  void failCreatePrivateChannel() throws Exception {
    // ids가 empty면 안됨
    List<UUID> ids = new ArrayList<>();
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(ids);

    assertThrows(IllegalChannelException.class, () -> channelService.createPrivate(request));

    verify(channelRepository, never()).save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 삭제")
  void deleteChannel() throws Exception {
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);

    doNothing().when(channelRepository).deleteById(channel.getId());

    channelService.remove(channel.getId());

    verify(channelRepository).deleteById(channel.getId());
  }

  @Test
  @DisplayName("채널 삭제 - 실패")
  void failDeleteChannel() throws Exception {
    // 존재하지 않은 채널 삭제 시도
    UUID channelId = UUID.randomUUID();

    doThrow(new ChannelNotFoundException(null, null, null))
        .when(channelRepository).deleteById(channelId);

    assertThrows(ChannelNotFoundException.class, () -> channelService.remove(channelId));

    verify(channelRepository).deleteById(channelId);
  }

  @Test
  @DisplayName("채널 수정")
  void modifyChannel() throws Exception {
    ChannelModifyRequest request = new ChannelModifyRequest("newCh", "new channel");
    Channel channel = createChannel(ChannelType.PUBLIC);
    ChannelDto channelDto = new ChannelDto(channel.getId(), ChannelType.PUBLIC, "newCh",
        "new channel", null, null);

    when(channelRepository.findById(channel.getId())).thenReturn(Optional.of(channel));
    when(channelMapper.toDto(any(Channel.class))).thenReturn(channelDto);

    ChannelDto updatedChannel = channelService.update(channel.getId(), request);

    assertNotNull(updatedChannel);
    assertEquals("newCh", updatedChannel.name());
    assertEquals(channel.getDescription(), updatedChannel.description());

  }

  @Test
  @DisplayName("채널 수정 - 실패")
  void failModifyChannel() throws Exception {
    // private 채널은 수정되면 안됨
    ChannelModifyRequest request = new ChannelModifyRequest("newCh", "new channel");
    Channel channel = createChannel(ChannelType.PRIVATE);

    when(channelRepository.findById(channel.getId())).thenReturn(Optional.of(channel));

    assertThrows(PrivateChannelCanNotModifyException.class,
        () -> channelService.update(channel.getId(), request));
  }

  @Test
  @DisplayName("유저가 참여중인 채널 목록 조회")
  void findAllChannelByUser() throws Exception {
    User user = new User("user1", "user1@mailcom", "password123", null, null);
    UUID userId = UUID.randomUUID();
    setUserId(user, userId);
    Channel channel1 = new Channel("ch1", "1st", ChannelType.PUBLIC);
    Channel channel2 = new Channel("ch2", "2nd", ChannelType.PUBLIC);
    Channel channel3 = new Channel("ch3", "3rd", ChannelType.PRIVATE);
    ReadStatus readStatus1 = new ReadStatus(user, channel1, Instant.now());
    ReadStatus readStatus2 = new ReadStatus(user, channel2, Instant.now());
    ReadStatus readStatus3 = new ReadStatus(user, channel3, Instant.now());
    List<ReadStatus> readStatuses = List.of(readStatus1, readStatus2, readStatus3);

    when(userRepository.existsById(userId)).thenReturn(true);
    when(readStatusRepository.findAllChannelsInUser(user.getId())).thenReturn(readStatuses);

    List<ChannelDto> channels = channelService.findAllChannelsByUserId(user.getId());

    assertNotNull(channels);
    assertEquals(3, channels.size());

    verify(readStatusRepository).findAllChannelsInUser(user.getId());
  }

  @Test
  @DisplayName("유저가 참여중인 채널 목록 조회 - 실패")
  void failFindAllChannelByUser() throws Exception {
    // 존재하지 않는 유저의 채널 목록 조회는 실패해야함
    UUID userId = UUID.randomUUID();

    assertThrows(UserNotFoundException.class, () -> channelService.findAllChannelsByUserId(userId));
  }


  private Channel createChannel(ChannelType channelType) {
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel("oldCh", "old channel", channelType);
    Field idField = ReflectionUtils.findField(Channel.class, "id");
    ReflectionUtils.makeAccessible(idField);
    ReflectionUtils.setField(idField, channel, channelId);

    return channel;
  }

  private void setUserId(User user, UUID userId) {
    Field idField = ReflectionUtils.findField(User.class, "id");
    ReflectionUtils.makeAccessible(idField);
    ReflectionUtils.setField(idField, user, userId);
  }
}