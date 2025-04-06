package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ChannelMapper channelMapper;

  @Mock
  private ReadStatusService readStatusService;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @InjectMocks
  private BasicChannelService channelService;

  @Nested
  class CreatePublicTest {

    @Test
    @DisplayName("공개 채널 생성 성공")
    void create_public_channel_success() {
      // given
      PublicChannelCreateRequest request = new PublicChannelCreateRequest("일반채널", "설명");

      Channel savedChannel = new Channel(Channel.ChannelType.PUBLIC, "일반채널", "설명");
      ReflectionTestUtils.setField(savedChannel, "id", UUID.randomUUID());

      ChannelDto channelDto = new ChannelDto(
          savedChannel.getId(),
          Channel.ChannelType.PUBLIC,
          "일반채널",
          "설명",
          null,
          Instant.now()
      );

      given(channelRepository.save(any(Channel.class))).willReturn(savedChannel);
      given(channelMapper.toDto(savedChannel)).willReturn(channelDto);

      // when
      ChannelDto result = channelService.create(request);

      // then
      assertEquals(ChannelType.PUBLIC, result.type());
      assertEquals("일반채널", result.name());
      assertEquals("설명", result.description());
    }

    @Test
    @DisplayName("공개 채널 생성 실패 - ")
    void create_public_channel_failure() {

    }
  }


  @Nested
  class CreatePrivateTest {

    @Test
    @DisplayName("비공개 채널 생성 성공")
    void create_private_channel_success() {
      // given
      List<UUID> participants = List.of(UUID.randomUUID(), UUID.randomUUID());
      PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participants);

      Channel savedChannel = new Channel(Channel.ChannelType.PRIVATE, null, null);
      ReflectionTestUtils.setField(savedChannel, "id", UUID.randomUUID());

      given(channelRepository.save(any(Channel.class))).willReturn(savedChannel);

      ChannelDto channelDto = new ChannelDto(
          savedChannel.getId(),
          ChannelType.PRIVATE,
          null,
          null,
          null,
          Instant.now()
      );
      given(channelMapper.toDto(savedChannel)).willReturn(channelDto);

      // when
      ChannelDto result = channelService.create(request);

      // then
      assertEquals(ChannelType.PRIVATE, result.type());
      then(readStatusService).should(times(participants.size())).create(any());
    }
  }
}
