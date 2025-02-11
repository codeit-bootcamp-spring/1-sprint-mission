package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.validation.ValidateChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

    @InjectMocks
    BasicChannelService basicChannelService;

    @Mock
    ChannelRepository channelRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    ReadStatusRepository readStatusRepository;

    @Mock
    ValidateChannel validateChannel;

    @BeforeEach
    void setUp(){

    }

    @Test
    public void createPrivateChannel_Success(){
        // given
        List<UUID> participants = new ArrayList<>(List.of(UUID.randomUUID(), UUID.randomUUID()));
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(ChannelType.PRIVATE, participants);
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);

        // when
        doNothing().when(validateChannel).validChannelType(any(ChannelType.class));

        when(channelRepository.save(any(Channel.class))).thenReturn(channel);

        ChannelDto result = basicChannelService.createPrivateChannel(request);

        // then
        assertNotNull(result);
        assertEquals(result.channelType(), request.channelType());

        verify(channelRepository, times(1)).save(any(Channel.class));
        verify(readStatusRepository, times(2)).save(any(ReadStatus.class));
    }

}