package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @InjectMocks
  private BasicChannelService basicChannelService;

  @Spy
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelMapper channelMapper;

  private PublicChannelCreateRequest publicChannelCreateRequest;
  private Channel publicChannel;
  private ChannelDto publicChannelDto;
  private PrivateChannelCreateRequest privateChannelCreateRequest;
  private Channel privateChannel;
  private ChannelDto privateChannelDto;

  @BeforeEach
  void setUp() {
    publicChannelCreateRequest = new PublicChannelCreateRequest("test1", "testDescription");
    publicChannel = new Channel(ChannelType.PUBLIC, publicChannelCreateRequest.name(),
        publicChannelCreateRequest.description());
    publicChannelDto = new ChannelDto(publicChannel.getId(), publicChannel.getType(),
        publicChannel.getName(), publicChannel.getDescription(), null, null);
    privateChannelCreateRequest = new PrivateChannelCreateRequest(List.of(UUID.randomUUID()));
    privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    privateChannelDto = new ChannelDto(privateChannel.getId(), privateChannel.getType(), null, null,
        null, null);
  }

  @Test
  void create_Success_PublicChannel() {
    // given
    given(channelRepository.save(any(Channel.class))).willReturn(publicChannel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

    // when
    ChannelDto result = basicChannelService.create(publicChannelCreateRequest);

    // then
    assertNotNull(result);
    assertEquals(result.name(), publicChannel.getName());
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  void create_Success_PrivateChannel() {
    // given
    given(channelRepository.save(any(Channel.class))).willReturn(privateChannel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(privateChannelDto);

    // when
    ChannelDto result = basicChannelService.create(privateChannelCreateRequest);

    // then
    assertNotNull(result);
    assertEquals(result.type(), privateChannel.getType());
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  void findAllByUserId_Success() {
    // given
    UUID userId = UUID.randomUUID();

    // Create Channel objects
    Channel publicChannel = Mockito.spy(new Channel(ChannelType.PUBLIC, "test", "test"));
    Channel privateChannel = Mockito.spy(new Channel(ChannelType.PRIVATE, null, null));

    // Mock channel IDs
    UUID privateChannelId = UUID.randomUUID();
    when(privateChannel.getId()).thenReturn(privateChannelId);

    // Create a list of UUIDs representing subscribed channel IDs
    List<UUID> subscribedChannelIds = List.of(privateChannel.getId());

    // Create list of channels to return from channelRepository
    List<Channel> channels = List.of(publicChannel, privateChannel);

    // Mock ReadStatus objects
    ReadStatus readStatus = Mockito.mock(ReadStatus.class);
    when(readStatus.getChannel()).thenReturn(privateChannel);

    // Mock the repository to return a list of ReadStatus
    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));

    // Mock channelRepository to return the channels
    given(
        channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, subscribedChannelIds)).willReturn(
        channels);

    // Create DTOs for expected return values
    List<ChannelDto> channelDtos = List.of(
        new ChannelDto(publicChannel.getId(), publicChannel.getType(), publicChannel.getName(),
            publicChannel.getDescription(), null, null),
        new ChannelDto(privateChannel.getId(), privateChannel.getType(), privateChannel.getName(),
            privateChannel.getDescription(), null, null));

    // Mock channelMapper to return DTOs for channels
    when(channelMapper.toDto(publicChannel)).thenReturn(channelDtos.get(0));
    when(channelMapper.toDto(privateChannel)).thenReturn(channelDtos.get(1));

    // when
    List<ChannelDto> result = basicChannelService.findAllByUserId(userId);

    // then
    assertNotNull(result);
  }

  @Test
  void update_Success() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription");
    Channel channel = new Channel(ChannelType.PUBLIC, "test", "test");
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    ChannelDto channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, channel.getName(),
        channel.getDescription(), null, null);
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    ChannelDto result = basicChannelService.update(channelId, request);

    // then
    assertNotNull(result);
    assertEquals(result.name(), channelDto.name());
  }

  @Test
  void update_Fail_NotFoundChannel() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription");
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when, then
    assertThrows(ChannelNotFoundException.class,
        () -> basicChannelService.update(channelId, request));
  }

  @Test
  void update_Fail_PrivateChannelUpdate() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription");
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

    // when, then
    assertThrows(PrivateChannelUpdateException.class,
        () -> basicChannelService.update(channelId, request));
  }

  @Test
  void delete_Success() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(true);

    // when
    basicChannelService.delete(channelId);

    // then
    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  void delete_Fail_NotFoundChannel() {
    // given
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);

    // when, then
    assertThrows(ChannelNotFoundException.class, () -> basicChannelService.delete(channelId));
  }
}