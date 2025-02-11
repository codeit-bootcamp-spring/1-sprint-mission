package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.validation.ValidateReadStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicReadStatusServiceTest {

    @InjectMocks
    BasicReadStatusService basicReadStatusService;

    @Mock
    ReadStatusRepository readStatusRepository;

    @Mock
    ValidateReadStatus validateReadStatus;

    @Test
    void create_Success(){
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        Instant lastReadTime = Instant.now();
        ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId, lastReadTime);
        ReadStatus readStatus = new ReadStatus(userId, channelId);

        // when
        doNothing().when(validateReadStatus).validateReadStatus(channelId, userId);
        when(readStatusRepository.save(any(ReadStatus.class))).thenReturn(readStatus);

        ReadStatusDto result = basicReadStatusService.create(request);

        // then
        assertNotNull(result);
        assertEquals(result.userId(), request.userId());
        assertEquals(result.channelId(), request.channelId());

        verify(readStatusRepository, times(1)).save(any(ReadStatus.class));
    }

    @Test
    void findById_Success(){
        // given
        ReadStatus readStatus = new ReadStatus();

        // when
        when(readStatusRepository.findById(readStatus.getId())).thenReturn(Optional.of(readStatus));

        ReadStatusDto result = basicReadStatusService.findById(readStatus.getId());

        // then
        assertNotNull(result);
        assertEquals(result.readStatusId(), readStatus.getId());

        verify(readStatusRepository, times(1)).findById(readStatus.getId());
    }

    @Test
    void findById_ReadStatusNotFound(){
        // given
        UUID readStatusId = UUID.randomUUID();

        // when
        when(readStatusRepository.findById(readStatusId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicReadStatusService.findById(readStatusId));
    }

    @Test
    void findAllByUserId_Success(){
        // given
        UUID userId = UUID.randomUUID();
        List<ReadStatus> readStatuses = new ArrayList<>(List.of(
                new ReadStatus(userId, UUID.randomUUID()),
                new ReadStatus(userId, UUID.randomUUID())
        ));

        // when
        when(readStatusRepository.findAllByUserId(userId)).thenReturn(readStatuses);

        List<ReadStatusDto> results = basicReadStatusService.findAllByUserId(userId);
        // then
        assertNotNull(results);
        assertEquals(2, results.size());

        verify(readStatusRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void update_Success(){
        // given
        ReadStatus readStatus = new ReadStatus();
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(readStatus.getId(), userId, channelId, Instant.now());

        // when
        when(readStatusRepository.findByUserIdAndChannelId(userId, channelId)).thenReturn(Optional.of(readStatus));
        when(readStatusRepository.save(any(ReadStatus.class))).thenReturn(readStatus);

        ReadStatusDto result = basicReadStatusService.update(request);

        // then
        assertNotNull(result);
        assertEquals(result.readStatusId(), request.readStatusId());

        verify(readStatusRepository, times(1)).findByUserIdAndChannelId(userId, channelId);
        verify(readStatusRepository, times(1)).save(any(ReadStatus.class));
    }

    @Test
    void update_ReadStatusNotFound(){
        // given
        ReadStatus readStatus = new ReadStatus();
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        ReadStatusUpdateRequest request = new ReadStatusUpdateRequest(readStatus.getId(), userId, channelId, Instant.now());

        // when
        when(readStatusRepository.findByUserIdAndChannelId(userId, channelId)).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicReadStatusService.update(request));

        verify(readStatusRepository, never()).save(any(ReadStatus.class));
    }

    @Test
    void delete_Success(){
        // given
        UUID readStatusId = UUID.randomUUID();

        // when
        basicReadStatusService.delete(readStatusId);

        // then
        verify(readStatusRepository, times(1)).delete(readStatusId);
    }
}