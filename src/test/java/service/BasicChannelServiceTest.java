package service;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {

  @Mock
  private ReadStatusService readStatusService;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private EntityValidator validator;
  @InjectMocks
  private BasicChannelService channelService;
  private Channel privateChannel;
  private Channel publicChannel;
  private User mockUser1;
  private User mockUser2;
  private ReadStatus readStatus;

  @BeforeEach
  void setUp(){
    mockUser1 = new User.UserBuilder("user1", "pwd1", "email1@email.com", "01012341234").nickname("nickname1").build();
    mockUser2 = new User.UserBuilder("user2", "pwd2", "email2@email.com", "01012344321").nickname("nickname2").build();

    privateChannel = new Channel.ChannelBuilder("channel1", Channel.ChannelType.VOICE)
        .isPrivate(true)
        .serverUUID("server1")
        .build();

    publicChannel = new Channel
        .ChannelBuilder("channel2", Channel.ChannelType.VOICE)
        .serverUUID("server1")
        .build();

    readStatus = new ReadStatus(privateChannel.getUUID(), mockUser1.getUUID());
  }

  @Test
  void testCreatePrivateChannel_Success(){
    CreatePrivateChannelDto channelDto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "server1",
        50,
        List.of(mockUser1.getUUID(), mockUser2.getUUID())
    );

    when(validator.findOrThrow(eq(User.class), eq(mockUser1.getUUID()), any(UserNotFoundException.class))).thenReturn(mockUser1);
    when(validator.findOrThrow(eq(User.class), eq(mockUser2.getUUID()), any(UserNotFoundException.class))).thenReturn(mockUser2);

    PrivateChannelResponseDto responseDto = channelService.createPrivateChannel(channelDto);

    assertThat(responseDto).isNotNull();
    assertThat(responseDto.channelId()).isNotNull();
    assertThat(responseDto.serverId()).isEqualTo("server1");
    assertThat(responseDto.channelType()).isEqualTo(Channel.ChannelType.VOICE);

    verify(channelRepository, times(1)).save(any());

    ArgumentCaptor<CreateReadStatusDto> captor = ArgumentCaptor.forClass(CreateReadStatusDto.class);

    verify(readStatusService, times(2)).create(captor.capture(), eq(true));

    List<CreateReadStatusDto> capturedValues = captor.getAllValues();
    assertThat(capturedValues).hasSize(2);
    assertThat(capturedValues).extracting("userId").containsExactlyInAnyOrder(mockUser1.getUUID(), mockUser2.getUUID());
    assertThat(capturedValues).extracting("channelId").containsOnly(responseDto.channelId());
  }

  @Test
  void testCreatePrivateChannel_Fail_UserNotFound(){
    CreatePrivateChannelDto channelDto = new CreatePrivateChannelDto(
        Channel.ChannelType.VOICE,
        "server1",
        50,
        List.of(mockUser1.getUUID())
    );

    when(validator.findOrThrow(eq(User.class), eq(mockUser1.getUUID()), any(UserNotFoundException.class))).thenThrow(UserNotFoundException.class);


    assertThatThrownBy(() -> channelService.createPrivateChannel(channelDto)).isInstanceOf(UserNotFoundException.class);
    verifyNoInteractions(channelRepository);
    verifyNoInteractions(readStatusService);
  }

  @Test
  void testCreatePublicChannel_Success(){
    CreateChannelDto channelDto = new CreateChannelDto(
        "channel1",
        Channel.ChannelType.VOICE,
        "server1",
        50
    );

    PublicChannelResponseDto responseDto = channelService.createPublicChannel(channelDto);

    assertThat(responseDto).isNotNull();
    assertThat(responseDto.channelId()).isNotNull();
    assertThat(responseDto.channelName()).isEqualTo("channel1");
    assertThat(responseDto.isPrivate()).isFalse();
    assertThat(responseDto.channelType()).isEqualTo(Channel.ChannelType.VOICE);

    verify(channelRepository, times(1)).save(any(Channel.class));
  }

  @Test
  void testGetChannelById_Success_PrivateChannel(){
    when(validator.findOrThrow(eq(Channel.class), eq(privateChannel.getUUID()), any(ChannelNotFoundException.class))).thenReturn(privateChannel);

    when(readStatusService.findAllByChannelId(privateChannel.getUUID())).thenReturn(List.of(new ReadStatus(privateChannel.getUUID(), mockUser1.getUUID())));

    FindChannelResponseDto responseDto = channelService.getChannelById(privateChannel.getUUID());

    assertThat(responseDto).isNotNull();
    assertThat(responseDto.channelId()).isEqualTo(privateChannel.getUUID());
    assertThat(responseDto.userIds()).containsOnly(mockUser1.getUUID());

    verify(readStatusService, times(1)).findAllByChannelId(privateChannel.getUUID());
    verify(messageRepository, times(1)).findLatestChannelMessage(privateChannel.getUUID());
  }

  @Test
  void testGetChannelById_Success_PublicChannel(){
    when(validator.findOrThrow(eq(Channel.class), eq(publicChannel.getUUID()), any(ChannelNotFoundException.class))).thenReturn(publicChannel);
    //when(channelRepository.findById(publicChannel.getUUID())).thenReturn(Optional.ofNullable(publicChannel));

    FindChannelResponseDto responseDto = channelService.getChannelById(publicChannel.getUUID());

    assertThat(responseDto).isNotNull();
    assertThat(responseDto.userIds()).isEmpty();
    assertThat(responseDto.channelId()).isEqualTo(publicChannel.getUUID());


    verify(readStatusService, times(1)).findAllByChannelId(publicChannel.getUUID());
    verify(messageRepository, times(1)).findLatestChannelMessage(publicChannel.getUUID());
  }

  @Test
  void testGetChannelById_Fail_ChannelNotFound(){

    when(validator.findOrThrow(eq(Channel.class), anyString(), any(ChannelNotFoundException.class))).thenThrow(ChannelNotFoundException.class );

    assertThatThrownBy(() -> channelService.getChannelById(privateChannel.getUUID())).isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  void testFindAllChannelsByUserId(){
    when(channelRepository.findAll()).thenReturn(List.of(privateChannel, publicChannel));
    when(readStatusService.findAllByUserId(mockUser1.getUUID())).thenReturn(List.of(readStatus));
    when(readStatusService.findAllByUserId(mockUser2.getUUID())).thenReturn(Collections.emptyList());

    List<FindChannelResponseDto> user1Response = channelService.findAllChannelsByUserId(mockUser1.getUUID());
    List<FindChannelResponseDto> user2Response = channelService.findAllChannelsByUserId(mockUser2.getUUID());

    assertThat(user1Response).hasSize(2);

    assertThat(user1Response).extracting("channelId").containsExactlyInAnyOrder(privateChannel.getUUID(), publicChannel.getUUID());

    assertThat(user2Response).hasSize(1);
    assertThat(user2Response).extracting("channelId").containsOnly(publicChannel.getUUID());
  }

  @Test
  void testUpdateChannel_Success(){
    ChannelUpdateDto updateDto = new ChannelUpdateDto(publicChannel.getUUID(), "newChannel", publicChannel.getMaxNumberOfPeople());

    when(channelRepository.findById(publicChannel.getUUID())).thenReturn(Optional.ofNullable(publicChannel));

    channelService.updateChannel(updateDto);

    verify(channelRepository, times(1)).findById(publicChannel.getUUID());
    verify(channelRepository, times(1)).update(any(Channel.class));
    assertThat(publicChannel.getChannelName()).isEqualTo("newChannel");
  }

  @Test
  void testUpdateChannel_Fail_ChannelNotFound(){
    ChannelUpdateDto updateDto = new ChannelUpdateDto(publicChannel.getUUID(), "newChannel", publicChannel.getMaxNumberOfPeople());

    when(channelRepository.findById(anyString())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> channelService.updateChannel(updateDto)).isInstanceOf(ChannelNotFoundException.class);
    verify(channelRepository, times(1)).findById(publicChannel.getUUID());
  }

  @Test
  void testUpdateChannel_Fail_ChannelIsPrivate(){
    ChannelUpdateDto updateDto = new ChannelUpdateDto(privateChannel.getUUID(), "newChannel", privateChannel.getMaxNumberOfPeople());

    when(channelRepository.findById(privateChannel.getUUID())).thenReturn(Optional.ofNullable(privateChannel));

    assertThatThrownBy(() -> channelService.updateChannel(updateDto)).isInstanceOf(InvalidOperationException.class);
    verify(channelRepository, times(1)).findById(privateChannel.getUUID());
  }

  @Test
  void testDeleteChannel_Success(){
    when(channelRepository.findById(privateChannel.getUUID())).thenReturn(Optional.ofNullable(privateChannel));

    channelService.deleteChannel(privateChannel.getUUID());
    verify(channelRepository, times(1)).findById(privateChannel.getUUID());
    verify(messageRepository, times(1)).deleteByChannel(privateChannel.getUUID());
    verify(channelRepository, times(1)).delete(privateChannel.getUUID());
  }

  @Test
  void testDeleteChannel_Fail(){
    when(channelRepository.findById(privateChannel.getUUID())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> channelService.deleteChannel(privateChannel.getUUID())).isInstanceOf(ChannelNotFoundException.class);

    verify(channelRepository, times(1)).findById(privateChannel.getUUID());
    verify(messageRepository, times(0)).deleteByChannel(privateChannel.getUUID());
    verify(channelRepository, times(0)).delete(privateChannel.getUUID());
  }
}
