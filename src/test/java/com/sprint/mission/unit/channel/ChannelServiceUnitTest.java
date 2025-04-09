package com.sprint.mission.unit.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.unit.TestEntityFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceUnitTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @InjectMocks
  private BasicChannelService basicChannelService;

  private User user;
  private Channel publicChannel;
  private Channel privateChannel;

  @BeforeEach
  void setUp() {
    user = TestEntityFactory.createUser("test", "t@gmail.com");
    publicChannel = TestEntityFactory.createPublicChannel();
    privateChannel = TestEntityFactory.createPrivateChannel();
  }

  @Nested
  class CreateChannel {

    @Test
    void createChannel_shouldCall_Success() {
      // given
      given(channelRepository.save(privateChannel)).willReturn(privateChannel);

      // when
      Channel result = basicChannelService.createPrivateChannel(privateChannel);

      //then
      assertThat(result).isEqualTo(privateChannel);
      then(channelRepository).should().save(privateChannel);
    }

    @Test
    void createChannel_shouldCall_fail() {
      //given
      given(channelRepository.save(privateChannel))
          .willThrow(DataIntegrityViolationException.class);

      // then when
      assertThatThrownBy(() -> basicChannelService.createPrivateChannel(privateChannel))
          .isInstanceOf(DataIntegrityViolationException.class);
    }
  }

  @Nested
  class ValidateAccess {

    @Test
    void validateUserAccess_shouldDoNothing_whenPublicChannel() {
      //when
      basicChannelService.validateUserAccess(publicChannel, user);

      //then
      then(readStatusRepository).shouldHaveNoInteractions();
    }

    @Test
    void validateUserAccess_shouldThrowException_emptyStatus() {
      //given
      given(readStatusRepository.findByUserAndChannel(user, privateChannel))
          .willReturn(Optional.empty());

      //when & then
      assertThatThrownBy(
          () -> basicChannelService.validateUserAccess(privateChannel, user))
          .isInstanceOf(ChannelException.class)
          .hasMessageContaining(ErrorCode.NO_ACCESS_TO_CHANNEL.getMessage());
    }

    @Test
    void validateUserAccess_shouldDoNothing_whenPrivateChannelWithAccess() {
      //given
      ReadStatus status = TestEntityFactory.createReadStatus(privateChannel, user);
      given(readStatusRepository.findByUserAndChannel(user, privateChannel))
          .willReturn(Optional.of(status));

      //when
      basicChannelService.validateUserAccess(privateChannel, user);

      // then
      then(readStatusRepository).should().findByUserAndChannel(user, privateChannel);
    }
  }

  @Nested
  class FindChannel {

    @Test
    void testFindChannelById_success() {
      // given
      String channelId = privateChannel.getId().toString();
      given(channelRepository.findById(UUID.fromString(channelId))).willReturn(
          Optional.ofNullable(privateChannel));

      // when
      Channel result = basicChannelService.findChannelById(channelId);

      // then

      assertThat(result).isEqualTo(privateChannel);
      then(channelRepository).should().findById(UUID.fromString(channelId));
    }

    @Test
    void testFindChannelById_notFound() {
      //given
      String channelId = privateChannel.getId().toString();
      given(channelRepository.findById(UUID.fromString(channelId)))
          .willReturn(Optional.empty());

      //when & then
      assertThatThrownBy(() -> basicChannelService.findChannelById(channelId))
          .isInstanceOf(ChannelNotFoundException.class)
          .hasMessageContaining(ErrorCode.CHANNEL_NOT_FOUND.getMessage());
    }

    @Test
    void testFindAllChannelsInOrPublic_shouldReturnList() {
      // given
      List<UUID> ids = List.of(privateChannel.getId(), publicChannel.getId());
      given(channelRepository.findByIdInOrType(ids, ChannelType.PUBLIC))
          .willReturn(List.of(privateChannel, publicChannel));

      // when
      List<Channel> result = basicChannelService.findAllChannelsInOrPublic(ids);

      // then
      assertThat(result).containsExactlyInAnyOrder(privateChannel, publicChannel);
      then(channelRepository).should().findByIdInOrType(ids, ChannelType.PUBLIC);
    }

  }

  @Nested
  class UpdateChannel {

    @Test
    void testUpdateChannel_success() {
      // given
      ChannelUpdateDto updateDto = new ChannelUpdateDto("updated", "updated description");
      Channel updatedChannel = TestEntityFactory.createPublicChannel();

      updatedChannel.updateChannelName(updateDto.newName());
      updatedChannel.updateDescription(updateDto.newDescription());

      given(channelRepository.findById(publicChannel.getId()))
          .willReturn(Optional.ofNullable(publicChannel));
      given(channelRepository.save(any(Channel.class)))
          .willReturn(updatedChannel);

      // when
      Channel result = basicChannelService.updateChannel(publicChannel.getId().toString(),
          updateDto);

      // then
      assertThat(result.getName()).isEqualTo("updated");
      then(channelRepository).should().findById(publicChannel.getId());
      then(channelRepository).should().save(publicChannel);
    }

    @Test
    void testUpdateChannel_shouldThrow_whenPrivateChannel() {
      // given
      ChannelUpdateDto dto = new ChannelUpdateDto("n", "n");
      given(channelRepository.findById(privateChannel.getId()))
          .willReturn(Optional.ofNullable(privateChannel));

      // when & then
      assertThatThrownBy(() ->
          basicChannelService.updateChannel(privateChannel.getId().toString(), dto))
          .isInstanceOf(PrivateChannelUpdateException.class)
          .hasMessageContaining(ErrorCode.PRIVATE_CHANNEL_CANNOT_BE_UPDATED.getMessage());
      then(channelRepository).should().findById(privateChannel.getId());
    }

    @Test
    void testUpdateChannel_shouldThrow_whenNotFound() {
      // given
      UUID id = UUID.randomUUID();
      ChannelUpdateDto dto = new ChannelUpdateDto("n", "n");
      given(channelRepository.findById(id)).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> basicChannelService.updateChannel(id.toString(), dto))
          .isInstanceOf(ChannelNotFoundException.class)
          .hasMessageContaining(ErrorCode.CHANNEL_NOT_FOUND.getMessage());
    }
  }

  @Nested
  class DeleteChannel {

    @Test
    void testDeleteChannel() {
      //given
      UUID id = UUID.randomUUID();

      //when
      basicChannelService.deleteChannel(id.toString());

      //then
      then(channelRepository).should().deleteById(id);
    }
  }

}
