package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
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
import java.util.Optional;
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
        when(channelRepository.save(any(Channel.class))).thenReturn(channel);

        ChannelDto result = basicChannelService.createPrivateChannel(request);

        // then
        assertNotNull(result);
        assertEquals(result.channelType(), request.channelType());

        verify(channelRepository, times(1)).save(any(Channel.class));
        verify(readStatusRepository, times(2)).save(any(ReadStatus.class));
    }

    @Test
    public void createPublicChannel_Success(){
        // given
        List<UUID> participants = new ArrayList<>(List.of(UUID.randomUUID(), UUID.randomUUID()));
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("디스코드", "코드잇", ChannelType.PUBLIC, participants);
        Channel channel = new Channel(request.name(), request.description(), request.channelType());

        // when
        doNothing().when(validateChannel).validatePublicChannel(anyString(), anyString());
        when(channelRepository.save(any(Channel.class))).thenReturn(channel);

        ChannelDto result = basicChannelService.createPublicChannel(request);

        // then
        assertNotNull(result);
        assertEquals(result.name(), request.name());
        assertEquals(result.description(), request.description());
        assertEquals(result.channelType(), request.channelType());

        verify(channelRepository, times(1)).save(any(Channel.class));
        verify(readStatusRepository, times(2)).save(any(ReadStatus.class));
    }

    @Test
    public void findById_Success(){
        // given
        Channel channel = new Channel("디스코드", "코드잇", ChannelType.PUBLIC);

        // when
        when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.of(channel));

        ChannelDto result = basicChannelService.findById(channel.getId());

        // then
        assertNotNull(result);
        assertEquals(result.channelId(), channel.getId());
        assertEquals(result.name(), channel.getName());
        assertEquals(result.channelType(), channel.getChannelType());

        verify(channelRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    public void findById_ChannelNotFound(){
        // given
        UUID channelId = UUID.randomUUID();

        // when
        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicChannelService.findById(channelId));
    }

    @Test
    public void findAllByUserId_Success(){
        // given
        UUID userID = UUID.randomUUID();
        List<Channel> channels = List.of(
           new Channel("디스코드1", "코드잇1", ChannelType.PUBLIC),
           new Channel("디스코드2", "코드잇2", ChannelType.PRIVATE),
           new Channel("디스코드3", "코드잇3", ChannelType.PRIVATE)
        );
        channels.get(0).addParticipant(userID);
        channels.get(1).addParticipant(userID);

        // when
        when(channelRepository.findAll()).thenReturn(channels);

        List<ChannelDto> results = basicChannelService.findAllByUserId(userID);

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(results.get(0).channelId(), channels.get(0).getId());

        verify(channelRepository, times(1)).findAll();
    }

    @Test
    public void update_Success(){
        // given
        Channel channel = new Channel("디스코드", "코드잇", ChannelType.PUBLIC);
        UUID channelId = channel.getId();
        UUID userId = UUID.randomUUID();
        channel.addParticipant(userId);

        UUID updateUserId = UUID.randomUUID();
        ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(channelId, "디스코드2", "코드잇2", ChannelType.PUBLIC, List.of(updateUserId));
        ReadStatus readStatus = new ReadStatus(userId, channelId);

        // when
        doNothing().when(validateChannel).validatePublicChannelType(any(ChannelType.class));
        doNothing().when(validateChannel).validatePublicChannel(anyString(),anyString());
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        when(readStatusRepository.findByUserIdAndChannelId(userId, channelId)).thenReturn(Optional.of(readStatus));

        ChannelDto result = basicChannelService.update(updateRequest);

        // then
        assertNotNull(result);

        verify(channelRepository, times(1)).findById(channelId);
        verify(readStatusRepository, times(1)).findByUserIdAndChannelId(userId, channelId);
        verify(readStatusRepository, times(1)).delete(readStatus.getId());
        verify(readStatusRepository, times(1)).deleteByChannelId(channelId);
        verify(readStatusRepository,times(1)).save(any(ReadStatus.class));
    }

    @Test
    public void update_ChannelNotFound(){
        // given
        UUID channelId = UUID.randomUUID();
        ChannelUpdateRequest request = new ChannelUpdateRequest(channelId, "디스코드", "코드잇", ChannelType.PUBLIC, List.of(UUID.randomUUID()));

        // when
        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicChannelService.update(request));

        verify(readStatusRepository, never()).findByUserIdAndChannelId(any(UUID.class), any(UUID.class));
        verify(readStatusRepository, never()).delete(any(UUID.class));
        verify(readStatusRepository, never()).deleteByChannelId(any(UUID.class));
        verify(readStatusRepository, never()).save(any(ReadStatus.class));
    }

    @Test
    public void update_ReadStatusNotFound(){
        // given
        Channel channel = new Channel("디스코드", "코드잇", ChannelType.PUBLIC);
        UUID channelId = channel.getId();
        UUID notExistsUserId = UUID.randomUUID();
        channel.addParticipant(notExistsUserId);

        UUID updateUserId = UUID.randomUUID();
        ChannelUpdateRequest request = new ChannelUpdateRequest(channelId, "디스코드", "코드잇", ChannelType.PUBLIC, List.of(updateUserId));

        // when
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        when(readStatusRepository.findByUserIdAndChannelId(notExistsUserId, channelId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicChannelService.update(request));

        verify(readStatusRepository, never()).deleteByChannelId(any(UUID.class));
        verify(readStatusRepository, never()).save(any(ReadStatus.class));
    }

    @Test
    public void delete_Success(){
        // given
        UUID channelId = UUID.randomUUID();

        // when
        basicChannelService.delete(channelId);

        // then
        verify(messageRepository, times(1)).deleteByChannelId(channelId);
        verify(readStatusRepository, times(1)).deleteByChannelId(channelId);
        verify(channelRepository, times(1)).deleteByChannelId(channelId);
    }
}