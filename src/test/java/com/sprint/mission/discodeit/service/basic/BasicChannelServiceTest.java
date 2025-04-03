package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.dto.channel.ChannelCreatePrivateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelCreatePublicDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelImmutableException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

  @DisplayName("공개 채널을 생성 할 수 있다")
  @Test
  void createPublicChannel() {
    //given
    ChannelCreatePublicDTO dto = new ChannelCreatePublicDTO("공개채널", "공개채널입니다.");
    Channel channel = new Channel(dto.getName(), dto.getDescription(), ChannelType.PUBLIC);
    UUID generatedId = UUID.randomUUID();

    given(channelRepository.save(any(Channel.class))).willAnswer(invocationOnMock -> {
      Channel ch = invocationOnMock.getArgument(0);

      ReflectionTestUtils.setField(ch, "id", generatedId);
      return ch;
    });

    ChannelDto expectedDto = new ChannelDto(generatedId, ChannelType.PUBLIC, dto.getName(),
        dto.getDescription(), null, null);
    given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

    //when
    ChannelDto result = channelService.create(dto);

    //then
    assertThat(result.getId()).isEqualTo(generatedId);
    assertThat(result.getName()).isEqualTo(dto.getName());
    assertThat(result.getType()).isEqualTo(ChannelType.PUBLIC);

    then(channelRepository).should().save(any(Channel.class));
    then(channelMapper).should().toDto(any(Channel.class));
  }

  @DisplayName("비공개 체널을 생성할 수 있다")
  @Test
  void createPrivateChannel() {
    //given
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    ChannelCreatePrivateDTO dto = new ChannelCreatePrivateDTO(List.of(userId1, userId2));

    User user1 = new User("user1", "user1@abc.com", "1234", null);
    User user2 = new User("user2", "user2@abc.com", "1234", null);

    Channel savedChannel = new Channel(null, null, ChannelType.PRIVATE);
    UUID generatedId = UUID.randomUUID();
    ReflectionTestUtils.setField(savedChannel, "id", generatedId);

    ChannelDto expectedDto = new ChannelDto(generatedId, ChannelType.PRIVATE, null, null, null,
        null);

    given(channelRepository.save(any(Channel.class))).willReturn(savedChannel);
    given(userRepository.findById(userId1)).willReturn(Optional.of(user1));
    given(userRepository.findById(userId2)).willReturn(Optional.of(user2));
    given(channelMapper.toDto(any(Channel.class))).willReturn(expectedDto);

    //when
    ChannelDto result = channelService.create(dto);

    //then
    assertThat(result.getId()).isEqualTo(generatedId);
    assertThat(result.getType()).isEqualTo(ChannelType.PRIVATE);

    then(channelRepository).should().save(any(Channel.class));
    then(readStatusRepository).should(times(2)).save(any(ReadStatus.class));
    then(channelMapper).should().toDto(any(Channel.class));
  }

  @DisplayName("존재하지 않은 유저를 가지고 비공개 채널을 생성할 수 없다.")
  @Test
  void failToCreatePrivateChannelIfUserNotFound() {
    //given
    UUID missId = UUID.randomUUID();
    ChannelCreatePrivateDTO dto = new ChannelCreatePrivateDTO(List.of(missId));

    given(channelRepository.save(any(Channel.class))).willReturn(
        new Channel(null, null, ChannelType.PRIVATE));
    given(userRepository.findById(missId)).willReturn(Optional.empty());

    //when //then
    assertThrows(UserNotFoundException.class, () -> channelService.create(dto));
  }

  @DisplayName("유저 id로 조회 시, 공개 채널과 참여 중인 비공개 채널만 반환한다.")
  @Test
  void findAllByUserId() {
    //given
    UUID userId = UUID.randomUUID();

    Channel publicChannel = new Channel("공개", "공개입니다", ChannelType.PUBLIC);
    Channel privateJoined = new Channel("비공개참여", null, ChannelType.PRIVATE);
    Channel privateNotJoined = new Channel("비공개비참여", null, ChannelType.PRIVATE);

    ReadStatus readStatus = new ReadStatus(null, privateJoined, Instant.EPOCH);
    given(readStatusRepository.findAllByUser_Id(userId)).willReturn(List.of(readStatus));

    given(channelRepository.findAll()).willReturn(
        List.of(publicChannel, privateJoined, privateNotJoined));

    ChannelDto dto1 = new ChannelDto();
    dto1.setName("공개");
    ChannelDto dto2 = new ChannelDto();
    dto2.setName("비공개");

    given(channelMapper.toDto(publicChannel)).willReturn(dto1);
    given(channelMapper.toDto(privateJoined)).willReturn(dto2);

    //when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    //then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(ChannelDto::getName)
        .containsExactlyInAnyOrder("공개", "비공개");

    then(readStatusRepository).should().findAllByUser_Id(userId);
    then(channelRepository).should().findAll();
    then(channelMapper).should().toDto(publicChannel);
    then(channelMapper).should().toDto(privateJoined);
    then(channelMapper).shouldHaveNoMoreInteractions();
  }

  @DisplayName("유저가 아무 채널에도 참여하지 않았고, 공개 채널도 없을 경우 빈 리스트를 반환한다")
  @Test
  void findAllByUserId_returnsEmptyList_whenNoPublicAndNoParticipation() {
    // given
    UUID userId = UUID.randomUUID();

    given(readStatusRepository.findAllByUser_Id(userId)).willReturn(List.of()); // 참여 채널 없음
    given(channelRepository.findAll()).willReturn(List.of()); // 전체 채널 없음

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).isEmpty();
    then(readStatusRepository).should().findAllByUser_Id(userId);
    then(channelRepository).should().findAll();
  }

  @DisplayName("공개 채널의 이름과 설명을 수정할 수 있다.")
  @Test
  void updatePublicChannel() {
    //given
    UUID channelId = UUID.randomUUID();

    ChannelUpdateDTO dto = new ChannelUpdateDTO("새이름", "새설명");
    Channel existingChannel = new Channel("기존이름", "기존설명", ChannelType.PUBLIC);
    ReflectionTestUtils.setField(existingChannel, "id", channelId);

    ChannelDto expectedDto = new ChannelDto(channelId, ChannelType.PUBLIC, dto.getNewName(),
        dto.getNewDescription(), null, null);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(existingChannel));
    given(channelMapper.toDto(existingChannel)).willReturn(expectedDto);

    //when
    ChannelDto result = channelService.update(channelId, dto);

    //then
    assertThat(result.getId()).isEqualTo(channelId);
    assertThat(result.getName()).isEqualTo("새이름");
    assertThat(result.getDescription()).isEqualTo("새설명");

    then(channelRepository).should().findById(channelId);
    then(channelMapper).should().toDto(existingChannel);
  }

  @DisplayName("비공개 채널은 수정할 수 없다.")
  @Test
  void updateChannel_fail_privateChannel() {
    //given
    UUID privateId = UUID.randomUUID();
    Channel privateChannel = new Channel(null, null, ChannelType.PRIVATE);
    ReflectionTestUtils.setField(privateChannel, "id", privateId);

    given(channelRepository.findById(privateId)).willReturn(Optional.of(privateChannel));

    //when //then
    assertThrows(PrivateChannelImmutableException.class,
        () -> channelService.update(privateId, new ChannelUpdateDTO("name", "desc")));

    then(channelRepository).should().findById(privateId);
    then(channelMapper).shouldHaveNoInteractions();
  }

  @DisplayName("채널은 삭제 가능하다.")
  @Test
  void delete() {
    // given
    UUID id = UUID.randomUUID();
    given(channelRepository.existsById(id)).willReturn(true);

    // when
    channelService.delete(id);

    // then
    then(channelRepository).should().existsById(id);
    then(channelRepository).should().deleteById(id);
  }

  @DisplayName("존재하지 않은 채널은 삭제할 수 없다.")
  @Test
  void deleteChannel_fail_channelNotFound() {
    // given
    UUID id = UUID.randomUUID();
    given(channelRepository.existsById(id)).willReturn(false);

    // when & then
    assertThrows(ChannelNotFoundException.class, () -> channelService.delete(id));

    then(channelRepository).should().existsById(id);
  }

}