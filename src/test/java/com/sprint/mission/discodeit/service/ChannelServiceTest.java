package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.Type;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.BinaryContentMapperImpl;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  ChannelRepository channelRepository;
  @Mock
  MessageRepository messageRepository;
  @Mock
  ReadStatusRepository readStatusRepository;
  @Mock
  BinaryContentStorage binaryContentStorage;

  @Mock
  ChannelMapper channelMapper;

  @InjectMocks
  ChannelService channelService;

  @Spy
  BinaryContentMapper binaryContentMapper = new BinaryContentMapperImpl();
  @Spy
  @InjectMocks
  UserMapper userMapper = new UserMapperImpl();


  @Test
  @DisplayName("channel 생성 - public")
  void createPublicChannel() {
    String name = "public channel";
    String description = "public channel입니다.";
    PublicChannelRequest publicChannelRequest = new PublicChannelRequest(name, description);
    Channel channel = Channel.create(Type.PUBLIC, publicChannelRequest.name(),
        publicChannelRequest.description());
    ReflectionTestUtils.setField(channel, "id", channel.getId());

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class)))
        .willReturn(ChannelDto.of(channel, channel.getCreatedAt(), null));

    ChannelDto publicChannel = channelService.createPublicChannel(publicChannelRequest);

    assertThat(publicChannel.id()).isEqualTo(channel.getId());
    assertThat(publicChannel.name()).isEqualTo(channel.getName());
    assertThat(publicChannel.description()).isEqualTo(channel.getDescription());
    assertThat(publicChannel.type()).isEqualTo(Type.PUBLIC);
  }

  @Test
  @DisplayName("channel 생성 - private")
  void createPrivateChannel() {
    List<UUID> userIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
    Channel channel = Channel.create(Type.PRIVATE, null, null);
    given(channelRepository.save(any(Channel.class))).willReturn(channel);

    User test1 = User.create("test1", "test1", "tset1");
    User test2 = User.create("test1", "test1", "tset1");
    ReflectionTestUtils.setField(test1, "id", userIds.get(0));
    ReflectionTestUtils.setField(test2, "id", userIds.get(1));
    List<UserDto> userRespons = Stream.of(test1, test2)
        .map(userMapper::toDto)
        .toList();
    given(userRepository.findById(test1.getId())).willReturn(Optional.of(test1));
    given(userRepository.findById(test2.getId())).willReturn(Optional.of(test2));
    given(channelMapper.toDto(any(Channel.class)))
        .willReturn(ChannelDto.of(channel, channel.getCreatedAt(), userRespons));

    ChannelDto privateChannel = channelService.createPrivateChannel(userIds);

    assertThat(privateChannel.id()).isEqualTo(channel.getId());
    assertThat(privateChannel.name()).isNull();
    assertThat(privateChannel.description()).isNull();
    assertThat(privateChannel.type()).isEqualTo(Type.PRIVATE);
    assertThat(privateChannel.participants()).containsExactlyInAnyOrder(userRespons.get(0),
        userRespons.get(1));
  }

  @Test
  @DisplayName("channel 조회")
  void readChannel() {
    UUID userId = UUID.randomUUID();
    Channel channel1 = Channel.create(Type.PUBLIC, "public", "public channel");
    Channel channel2 = Channel.create(Type.PRIVATE, null, null);
    Channel channel3 = Channel.create(Type.PRIVATE, null, null);
    ReflectionTestUtils.setField(channel1, "id", UUID.randomUUID());
    ReflectionTestUtils.setField(channel2, "id", UUID.randomUUID());
    ReflectionTestUtils.setField(channel3, "id", UUID.randomUUID());

    ReadStatus readStatus = ReadStatus.create(User.create("test", "test", "test"), channel2);

    given(userRepository.existsById(userId)).willReturn(true);
    given(readStatusRepository.findByUserId(userId)).willReturn(List.of(readStatus));
    given(channelRepository.findAll()).willReturn(List.of(channel1, channel2, channel3));

    ChannelDto publicChannelDto = ChannelDto.of(channel1, channel1.getCreatedAt(),
        null);
    ChannelDto privateChannelDto = ChannelDto.of(channel2, channel2.getCreatedAt(),
        null);
    given(channelMapper.toDto(channel1)).willReturn(publicChannelDto);
    given(channelMapper.toDto(channel2)).willReturn(privateChannelDto);

    List<ChannelDto> channelRespons = channelService.readAllByUserId(userId);

    assertThat(channelRespons)
        .containsExactlyInAnyOrder(publicChannelDto, privateChannelDto);
    assertThat(channelRespons)
        .doesNotContain(ChannelDto.of(channel3, channel3.getCreatedAt(), null));
  }

  @Test
  @DisplayName("channel 조회 실패 - userId가 존재하지 않는 경우")
  void failReadChannel() {
    UUID userId = UUID.randomUUID();
    given(userRepository.existsById(userId)).willReturn(false);

    assertThatThrownBy(() -> channelService.readAllByUserId(userId))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("channel 수정 - public")
  void updatePublicChannel() {
    UUID channelId = UUID.randomUUID();
    String newName = "newName";
    String newDescription = "newDescription";
    PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest(newName,
        newDescription);

    Channel channel = Channel.create(Type.PUBLIC, "name", "description");
    ReflectionTestUtils.setField(channel, "id", channelId);
    given(channelRepository.findById(channelId))
        .willReturn(Optional.of(channel));
    given(channelMapper.toDto(channel))
        .willAnswer(i -> ChannelDto.of(channel, channel.getCreatedAt(), null));

    ChannelDto channelDto = channelService.updateChannel(channelId,
        publicChannelUpdateRequest);

    assertThat(channelDto.id()).isEqualTo(channelId);
    assertThat(channelDto.name()).isEqualTo(newName);
    assertThat(channelDto.description()).isEqualTo(newDescription);
    assertThat(channelDto.type()).isEqualTo(Type.PUBLIC);
  }

  @Test
  @DisplayName("channel 수정 실패 - channel이 없는 경우")
  void failUpdateChannel() {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.create(Type.PUBLIC, null, null);
    ReflectionTestUtils.setField(channel, "id", channelId);

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> channelService.updateChannel(channelId, null))
        .isInstanceOf(ChannelNotFoundException.class);

  }

  @Test
  @DisplayName("channel 수정 실패 - private인 경우")
  void failUpdatePrivateChannel() {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.create(Type.PRIVATE, null, null);
    ReflectionTestUtils.setField(channel, "id", channelId);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    assertThatThrownBy(() -> channelService.updateChannel(channelId, null))
        .isInstanceOf(PrivateChannelUpdateException.class);
  }

  @Test
  @DisplayName("channel 삭제 - channel이 없는 경우")
  void deleteChannel() {
    UUID channelId = UUID.randomUUID();

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    channelService.deleteChannel(channelId);

    verify(binaryContentStorage, never()).delete(any(UUID.class));
    verify(messageRepository, never()).delete(any(Message.class));
    verify(channelRepository, never()).delete(any(Channel.class));
  }

  @Test
  @DisplayName("channel 삭제")
  void deleteExistChannel() {
    UUID channelId = UUID.randomUUID();
    Channel channel = Channel.create(Type.PUBLIC, null, null);
    ReflectionTestUtils.setField(channel, "id", channelId);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(messageRepository.findByChannelIdWithAttachments(channelId)).willReturn(List.of());

    channelService.deleteChannel(channelId);
    
    verify(readStatusRepository).deleteByChannel(channel);
    verify(channelRepository).delete(channel);
  }
}