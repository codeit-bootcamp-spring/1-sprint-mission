package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
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
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  private Channel publicChannel;
  private Channel privateChannel;
  private ChannelDto publicChannelDto;
  private ChannelDto privateChannelDto;
  private PublicChannelCreateRequest publicRequest;
  private PrivateChannelCreateRequest privateRequest;
  private PublicChannelUpdateRequest updateRequest;
  private UUID userId;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    userId = UUID.randomUUID();
    user = new User("testUser", "test@example.com", "password", null);
    user.setId(userId);
    userDto = new UserDto(userId, "testUser", "test@example.com", null, true);
    publicChannel = new Channel(ChannelType.PUBLIC, "publicChannel", "desc");
    publicChannel.setId(channelId);
    privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    privateChannel.setId(channelId);
    publicChannelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "publicChannel", "desc", Collections.emptyList(), null);
    privateChannelDto = new ChannelDto(channelId, ChannelType.PRIVATE, null, null, List.of(userDto), null);
    publicRequest = new PublicChannelCreateRequest("publicChannel", "desc");
    privateRequest = new PrivateChannelCreateRequest(List.of(userId));
    updateRequest = new PublicChannelUpdateRequest("newName", "newDesc");
  }

  // create (PUBLIC) 메서드 테스트
  @Test
  @DisplayName("공개 채널 생성 성공")
  void create_PublicChannel_ShouldSucceed() {
    // Given
    given(channelRepository.save(any(Channel.class))).willReturn(publicChannel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

    // When
    ChannelDto result = channelService.create(publicRequest);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo(publicRequest.name());
    assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
    assertThat(result.participants()).isEmpty();
  }

  @Test
  @DisplayName("공개 채널 생성 성공 - 설명 없이")
  void create_PublicChannelWithoutDesc_ShouldSucceed() {
    // Given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("channel", null);
    Channel channel = new Channel(ChannelType.PUBLIC, "channel", null);
    ChannelDto dto = new ChannelDto(channelId, ChannelType.PUBLIC, "channel", null, Collections.emptyList(), null);
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(dto);

    // When
    ChannelDto result = channelService.create(request);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo(request.name());
    assertThat(result.description()).isNull();
  }

  // create (PRIVATE) 메서드 테스트
  @Test
  @DisplayName("비공개 채널 생성 성공")
  void create_PrivateChannel_ShouldSucceed() {
    // Given
    given(channelRepository.save(any(Channel.class))).willReturn(privateChannel);
    given(userRepository.findAllById(privateRequest.participantIds())).willReturn(List.of(user));
    given(readStatusRepository.saveAll(any())).willReturn(Collections.emptyList());
    given(channelMapper.toDto(any(Channel.class))).willReturn(privateChannelDto);

    // When
    ChannelDto result = channelService.create(privateRequest);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
    assertThat(result.participants()).hasSize(1);
    assertThat(result.participants().get(0).username()).isEqualTo("testUser");
  }

  // find 메서드 테스트
  @Test
  @DisplayName("채널 조회 성공")
  void find_ShouldReturnChannelDto() {
    // Given
    given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

    // When
    ChannelDto result = channelService.find(channelId);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(channelId);
    assertThat(result.participants()).isEmpty();
  }

  @Test
  @DisplayName("채널 조회 실패 - 존재하지 않는 채널")
  void find_WithNonExistentChannel_ShouldThrowException() {
    // Given
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> channelService.find(channelId))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessage("채널을 찾을 수 없습니다.");
  }

  // findAllByUserId 메서드 테스트
  @Test
  @DisplayName("사용자별 채널 목록 조회 성공 - 공개 채널만")
  void findAllByUserId_PublicOnly_ShouldSucceed() {
    // Given
    given(readStatusRepository.findAllByUserId(userId)).willReturn(Collections.emptyList());
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, Collections.emptyList()))
        .willReturn(List.of(publicChannel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

    // When
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).type()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  @DisplayName("사용자별 채널 목록 조회 성공 - 비공개 채널 포함")
  void findAllByUserId_WithPrivate_ShouldSucceed() {
    // Given
    ReadStatus status = new ReadStatus(user, privateChannel, Instant.now());
    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(status));
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of(channelId)))
        .willReturn(List.of(publicChannel, privateChannel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto)
        .willReturn(privateChannelDto);

    // When
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).anyMatch(dto -> dto.type() == ChannelType.PRIVATE);
  }

  // update 메서드 테스트
  @Test
  @DisplayName("공개 채널 업데이트 성공")
  void update_PublicChannel_ShouldSucceed() {
    // Given
    given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

    // When
    ChannelDto result = channelService.update(channelId, updateRequest);

    // Then
    assertThat(result).isNotNull();
    assertThat(publicChannel.getName()).isEqualTo(updateRequest.newName());
  }

  @Test
  @DisplayName("채널 업데이트 실패 - 비공개 채널")
  void update_PrivateChannel_ShouldThrowException() {
    // Given
    given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

    // When & Then
    assertThatThrownBy(() -> channelService.update(channelId, updateRequest))
        .isInstanceOf(PrivateChannelUpdateNotAllowedException.class)
        .hasMessage("비공개 채널은 수정할 수 없습니다.");
  }

  @Test
  @DisplayName("채널 업데이트 실패 - 존재하지 않는 채널")
  void update_NonExistentChannel_ShouldThrowException() {
    // Given
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> channelService.update(channelId, updateRequest))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessage("채널을 찾을 수 없습니다.");
  }

  // delete 메서드 테스트
  @Test
  @DisplayName("채널 삭제 성공")
  void delete_ShouldSucceed() {
    // Given
    given(channelRepository.existsById(channelId)).willReturn(true);
    willDoNothing().given(messageRepository).deleteAllByChannelId(channelId);
    willDoNothing().given(readStatusRepository).deleteAllByChannelId(channelId);
    willDoNothing().given(channelRepository).deleteById(channelId);

    // When
    channelService.delete(channelId);

    // Then
    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
  }

  @Test
  @DisplayName("채널 삭제 실패 - 존재하지 않는 채널")
  void delete_NonExistentChannel_ShouldThrowException() {
    // Given
    given(channelRepository.existsById(channelId)).willReturn(false);

    // When & Then
    assertThatThrownBy(() -> channelService.delete(channelId))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessage("채널을 찾을 수 없습니다.");
  }
}