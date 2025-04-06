package com.sprint.mission.discodeit.unitTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ChannelMapper channelMapper;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private BasicChannelService channelService;

  private UUID channelId;
  private Channel publicChannel;
  private Channel privateChannel;

  private ChannelDto channelDto;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    publicChannel = new Channel(ChannelType.PUBLIC, "Codeit Channel", "Public Codeit Channel");
    privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    channelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "Codeit Channel",
        "Public Codeit Channel",
        List.of(new UserDto(UUID.randomUUID(), "woody", "woody@naver.com", null, true),
            new UserDto(UUID.randomUUID(), "buzz", "buzz@naver.com", null, false)), Instant.now());
  }

  @Test
  void shouldCreatePublicChannelSuccess() {
    //given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("Codeit Channel",
        "Public Codeit Channel");
    given(channelMapper.toDto(any())).willReturn(channelDto);
    given(channelRepository.save(any())).willReturn(publicChannel);

    //when
    ChannelDto result = channelService.create(request);

    //then
    assertThat(result).isNotNull();
    assertThat(result.name()).isEqualTo("Codeit Channel");
    verify(channelRepository, times(1)).save(any());
  }

  @Test
  void shouldCreatePrivateChannelSuccess() {
    //given
    PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
        List.of(UUID.randomUUID()));
    given(channelRepository.save(any())).willReturn(privateChannel);
    given(channelMapper.toDto(any())).willReturn(channelDto);

    //when
    ChannelDto result = channelService.create(request);

    //then
    assertThat(result).isNotNull();
    verify(channelRepository, times(1)).save(any());
    verify(readStatusRepository, times(1)).saveAll(any());
  }

  @Test
  void shouldUpdatePublicChannelSuccess() {
    //given
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("Public Codeit Channel",
        "Codeit's Public Channel");
    given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
    given(channelMapper.toDto(any())).willReturn(channelDto);

    //when
    ChannelDto result = channelService.update(channelId, request);

    //then
    assertThat(result).isNotNull();
    verify(channelRepository, times(1)).findById(channelId);
  }

  @Test
  void shouldDeleteChannelSuccess() {
    //given
    given(channelRepository.existsById(channelId)).willReturn(true);

    //when
    channelService.delete(channelId);

    //then
    verify(messageRepository, times(1)).deleteAllByChannelId(channelId);
    verify(readStatusRepository, times(1)).deleteAllByChannelId(channelId);
    verify(channelRepository, times(1)).deleteById(channelId);
  }

  @Test
  void shouldThrowExceptionWhenDeleteNonExistingChannel() {
    given(channelRepository.existsById(channelId)).willReturn(false);

    //when & then
    assertThatThrownBy(() -> channelService.delete(channelId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("Channel with id " + channelId + " not found");
  }
}