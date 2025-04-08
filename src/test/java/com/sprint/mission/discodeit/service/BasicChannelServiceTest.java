package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNameAlreadyExistsException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService channelService;

  @Test
  @DisplayName("공개 채널 생성 - 성공")
  void createPublicChannelSuccess() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "테스트 채널입니다.");
    Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description());
    ChannelDto channelDto = new ChannelDto(channel.getId(), ChannelType.PUBLIC, request.name(),
        request.description(), null,
        Instant.now());

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    ChannelDto result = channelService.create(request);
    assertThat(result.name()).isEqualTo("test");
  }

  @Test
  @DisplayName("공개 채널 생성 - 실패 (중복된 이름)")
  void createPublicChannelFail_DuplicatedName() {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("test", "테스트 채널입니다.");

    given(channelRepository.existsChannelByName(request.name())).willReturn(true);

    assertThatThrownBy(() -> channelService.create(request))
        .isInstanceOf(ChannelNameAlreadyExistsException.class)
        .hasMessageContaining("중복된 채널 이름입니다.");

  }

  @Test
  @DisplayName("개인 채널 생성 - 성공")
  void createPrivateChannelSuccess() {
    List<UUID> participantIds = List.of(UUID.randomUUID(), UUID.randomUUID());
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    ChannelDto channelDto = new ChannelDto(channel.getId(), ChannelType.PRIVATE,
        null, null, null,
        Instant.now());

    List<User> users = participantIds.stream()
        .map(id -> new User("user" + id, "user" + id + "@example.com", "pw", null))
        .toList();

    List<ReadStatus> readStatuses = users.stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(userRepository.findAllById(participantIds)).willReturn(users);
    given(readStatusRepository.saveAll(anyList())).willReturn(readStatuses);
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    ChannelDto result = channelService.create(request);
    then(channelRepository).should().save(any());
  }

  @Test
  @DisplayName("개인 채널 생성 실패 - 존재하지 않는 유저 ID 포함")
  void createPrivateChannelFail_UserNotFound() {
    UUID user1 = UUID.randomUUID();
    UUID user2 = UUID.randomUUID();
    List<UUID> requestIds = List.of(user1, user2);

    given(userRepository.findAllById(requestIds))
        .willReturn(List.of(new User("user1", "email1@test.com", "password", null)));

    assertThatThrownBy(() -> channelService.create(new PrivateChannelCreateRequest(requestIds)))
        .isInstanceOf(UserNotFoundException.class);
  }

  @DisplayName("공개 채널 수정 성공")
  @Test
  void updatePublicChannelSuccess() {
    UUID channelId = UUID.randomUUID();
    Channel channel = new Channel(ChannelType.PUBLIC, "oldName", "oldDesc");
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "newDesc");
    ChannelDto expectedDto = new ChannelDto(channelId, ChannelType.PUBLIC, "newName", "newDesc",
        null, Instant.now());

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelMapper.toDto(channel)).willReturn(expectedDto);

    ChannelDto result = channelService.update(channelId, request);

    assertThat(result.name()).isEqualTo("newName");
    assertThat(result.description()).isEqualTo("newDesc");
  }

  @Test
  @DisplayName("공개 채널 수정 실패 - 채널 없음")
  void updateChannelFail_NotFound() {
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "newDesc");

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> channelService.update(channelId, request))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @DisplayName("채널 삭제 성공")
  @Test
  void deleteChannelSuccess() {
    UUID channelId = UUID.randomUUID();

    given(channelRepository.existsById(channelId)).willReturn(true);
    willDoNothing().given(messageRepository).deleteAllByChannelId(channelId);
    willDoNothing().given(readStatusRepository).deleteAllByChannelId(channelId);
    willDoNothing().given(channelRepository).deleteById(channelId);

    channelService.delete(channelId);

    then(channelRepository).should().existsById(channelId);
    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  @DisplayName("채널 삭제 실패 - 채널 없음")
  void deleteChannelFail_ChannelNotFound() {
    UUID channelId = UUID.randomUUID();
    given(channelRepository.existsById(channelId)).willReturn(false);

    assertThatThrownBy(() -> channelService.delete(channelId))
        .isInstanceOf(ChannelNotFoundException.class);

    then(channelRepository).should().existsById(channelId);
    then(messageRepository).should(never()).deleteAllByChannelId(any());
    then(readStatusRepository).should(never()).deleteAllByChannelId(any());
    then(channelRepository).should(never()).deleteById(any());
  }

  @Test
  @DisplayName("유저의 채널 전체 조회 - 성공")
  void findAllByUserIdSuccess() {
    UUID userId = UUID.randomUUID();

    Channel publicChannel = new Channel(ChannelType.PUBLIC, "공개 채널", "공개 설명");
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);

    ReadStatus readStatus = new ReadStatus(
        new User("test", "test@email.com", "1234", null),
        privateChannel,
        Instant.now());

    List<ReadStatus> readStatuses = Arrays.asList(readStatus);
    List<UUID> channelIds = Arrays.asList(privateChannel.getId());

    ChannelDto publicChannelDto = new ChannelDto(publicChannel.getId(), ChannelType.PUBLIC, "공개 채널",
        "공개 설명", null, Instant.now());
    ChannelDto privateChannelDto = new ChannelDto(privateChannel.getId(), ChannelType.PRIVATE, null,
        null, null, Instant.now());

    given(readStatusRepository.findAllByUserId(userId)).willReturn(readStatuses);
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, channelIds)).willReturn(
        List.of(publicChannel, privateChannel));
    given(channelMapper.toDto(publicChannel)).willReturn(publicChannelDto);
    given(channelMapper.toDto(privateChannel)).willReturn(privateChannelDto);

    List<ChannelDto> result = channelService.findAllByUserId(userId);

    assertThat(result).hasSize(2);
    then(readStatusRepository).should().findAllByUserId(userId);
    then(channelRepository).should().findAllByTypeOrIdIn(ChannelType.PUBLIC, channelIds);
    then(channelMapper).should(times(2)).toDto(any(Channel.class));
  }

  @DisplayName("유저의 채널 전체 조회 - 구독한 채널 없음")
  @Test
  void findAllByUserId_NoSubscribedChannels() {
    UUID userId = UUID.randomUUID();
    List<ReadStatus> emptyReadStatuses = List.of();

    Channel publicChannel = new Channel(ChannelType.PUBLIC, "공개 채널", "공개 설명");
    ChannelDto publicChannelDto = new ChannelDto(publicChannel.getId(), ChannelType.PUBLIC, "공개 채널",
        "공개 설명", null, Instant.now());

    given(readStatusRepository.findAllByUserId(userId)).willReturn(emptyReadStatuses);
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of())).willReturn(
        List.of(publicChannel));
    given(channelMapper.toDto(publicChannel)).willReturn(publicChannelDto);

    List<ChannelDto> result = channelService.findAllByUserId(userId);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).name()).isEqualTo("공개 채널");

    then(readStatusRepository).should().findAllByUserId(userId);
    then(channelRepository).should().findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());
    then(channelMapper).should().toDto(publicChannel);
  }
}
