package com.sprint.mission.unit.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.lenient;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.channel.ChannelManagementService;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.facade.channel.ChannelMasterFacadeImpl;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChannelFacadeUnitTest {

  @Mock
  private ChannelManagementService channelManagementService;
  @Mock
  private ChannelService channelService;
  @Mock
  private MessageService messageService;
  @Mock
  private ChannelMapper channelMapper;
  @Mock
  private UserService userService;
  @Mock
  private ReadStatusService readStatusService;

  @InjectMocks
  private ChannelMasterFacadeImpl channelMasterFacade;


  @Nested
  class CreateChannel {

    @Test
    void createPrivateChannel_shouldCall() {
      List<String> userIds = List.of(UUID.randomUUID().toString());
      CreatePrivateChannelDto dto = new CreatePrivateChannelDto(userIds);
      Channel channel = mock(Channel.class);
      User user = mock(User.class);
      ReadStatus status = mock(ReadStatus.class);
      ChannelResponseDto responseDto = mock(ChannelResponseDto.class);

      given(channelMapper.toEntity(dto)).willReturn(channel);
      given(channelManagementService.createPrivateChannel(channel, userIds)).willReturn(channel);
      given(channel.getStatuses()).willReturn(List.of(status));
      given(status.getUser()).willReturn(user);
      given(channelMapper.toDto(channel, Instant.EPOCH, List.of(user)))
          .willReturn(responseDto);

      // when
      ChannelResponseDto response = channelMasterFacade.createPrivateChannel(dto);

      //then
      assertThat(response).isEqualTo(responseDto);
      then(channelMapper).should().toEntity(dto);
      then(channelManagementService).should().createPrivateChannel(channel, userIds);
      then(channelMapper).should().toDto(channel, Instant.EPOCH, List.of(user));
    }

    @Test
    void createPublicChannel_shouldCall() {
      CreateChannelDto privateChannelDto = new CreateChannelDto("name", "description");
      Channel channel = mock(Channel.class);
      ChannelResponseDto responseDto = mock(ChannelResponseDto.class);

      given(channelManagementService.createPublicChannel(any(Channel.class)))
          .willReturn(channel);
      given(channelMapper.toEntity(privateChannelDto))
          .willReturn(channel);
      given(channelMapper.toDto(channel, Instant.EPOCH, List.of()))
          .willReturn(responseDto);

      //when
      ChannelResponseDto response = channelMasterFacade.createPublicChannel(privateChannelDto);

      // then
      assertThat(response).isEqualTo(responseDto);
      then(channelMapper).should().toEntity(privateChannelDto);
      then(channelManagementService).should().createPublicChannel(any(Channel.class));
      then(channelMapper).should().toDto(channel, Instant.EPOCH, List.of());
    }

  }

  @Nested
  class FindChannel {

    @Test
    void getChannelById_shouldCall() {
      // given
      String channelId = UUID.randomUUID().toString();
      Channel channel = mock(Channel.class);
      ReadStatus status = mock(ReadStatus.class);
      User user = mock(User.class);
      Instant lastMessageTime = Instant.now();
      ChannelResponseDto responseDto = mock(ChannelResponseDto.class);

      given(channelService.findChannelById(channelId)).willReturn(channel);
      given(channel.getStatuses()).willReturn(List.of(status));
      given(status.getUser()).willReturn(user);
      given(channelManagementService.getLastMessageTimeForChannel(channelId)).willReturn(
          lastMessageTime);
      given(channelMapper.toDto(channel, lastMessageTime, List.of(user))).willReturn(responseDto);

      // when
      ChannelResponseDto result = channelMasterFacade.getChannelById(channelId);

      // then
      assertThat(result).isEqualTo(responseDto);
      then(channelService).should().findChannelById(channelId);
      then(channelManagementService).should().getLastMessageTimeForChannel(channelId);
      then(channelMapper).should().toDto(channel, lastMessageTime, List.of(user));
    }

    @Test
    void testFindAllChannelsByUserIdV3_shouldCall() {
      // given
      String userId = UUID.randomUUID().toString();
      Channel channel = mock(Channel.class);
      UUID channelId = UUID.randomUUID();
      given(channel.getId()).willReturn(channelId);
      lenient().when(channel.getId()).thenReturn(channelId);

      List<Channel> channels = List.of(channel);
      Instant messageTime = Instant.now();

      Map<UUID, Instant> messageMap = Map.of(channelId, messageTime);
      Map<UUID, List<User>> participantsMap = Map.of(channelId, List.of(mock(User.class)));
      List<ChannelResponseDto> expected = List.of(mock(ChannelResponseDto.class));

      given(channelManagementService.findAllChannelsForUser(userId)).willReturn(channels);
      given(messageService.getLatestMessageForChannels(channels)).willReturn(messageMap);
      given(channelMapper.channelToParticipants(channels)).willReturn(participantsMap);
      given(channelMapper.toListResponse(channels, messageMap, participantsMap)).willReturn(
          expected);

      // when
      List<ChannelResponseDto> result = channelMasterFacade.findAllChannelsByUserIdV3(userId);

      // then
      assertThat(result).isEqualTo(expected);
    }

  }

  @Nested
  class UpdateChannel {

    @Test
    void updateChannel_shouldCall() {
      String channelId = UUID.randomUUID().toString();
      ChannelUpdateDto updateDto = new ChannelUpdateDto("newName", "newDescription");

      Channel channel = mock(Channel.class);
      ReadStatus status = mock(ReadStatus.class);
      User user = mock(User.class);
      List<UUID> ids = List.of(UUID.randomUUID());
      Instant lastMessageTime = Instant.now();
      ChannelResponseDto responseDto = mock(ChannelResponseDto.class);
      given(channelService.updateChannel(channelId, updateDto)).willReturn(channel);
      given(readStatusService.findAllByChannelId(channelId)).willReturn(List.of(status));
      given(status.getUser()).willReturn(user);
      given(user.getId()).willReturn(ids.get(0));
      given(userService.findByAllIn(ids)).willReturn(List.of(user));
      given(channelManagementService.getLastMessageTimeForChannel(channelId)).willReturn(
          lastMessageTime);
      given(channelMapper.toDto(channel, lastMessageTime, List.of(user))).willReturn(responseDto);

      // when
      ChannelResponseDto result = channelMasterFacade.updateChannel(channelId, updateDto);

      // then
      assertThat(result).isEqualTo(responseDto);
      then(channelService).should().updateChannel(channelId, updateDto);
      then(readStatusService).should().findAllByChannelId(channelId);
      then(userService).should().findByAllIn(ids);
      then(channelManagementService).should().getLastMessageTimeForChannel(channelId);
      then(channelMapper).should().toDto(channel, lastMessageTime, List.of(user));
    }
  }

  @Nested
  class DeleteChannel {

    @Test
    void deleteChannel_shouldCallService_whenValidId() {
      // given
      String channelId = UUID.randomUUID().toString();

      // when
      channelMasterFacade.deleteChannel(channelId);

      // then
      then(channelService).should().deleteChannel(channelId);
    }

    @Test
    void deleteChannel_shouldThrow_whenBlankId() {
      // when & then
      assertThatThrownBy(() -> channelMasterFacade.deleteChannel(" "))
          .isInstanceOf(DiscodeitException.class)
          .hasMessageContaining(ErrorCode.INVALID_UUID_FORMAT.getMessage());
    }
  }

}
