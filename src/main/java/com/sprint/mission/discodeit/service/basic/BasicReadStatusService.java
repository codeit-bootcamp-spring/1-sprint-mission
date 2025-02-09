package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.validation.ValidateReadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ValidateReadStatus validateReadStatus;

    public ReadStatusDto create(ReadStatusCreateRequest request) {
        validateReadStatus.validateReadStatus(request.channelId(), request.userId());

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId());
        readStatusRepository.save(readStatus);
        return changeToDto(readStatus);
    }

    public ReadStatusDto findById(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("ReadStatus not found."));
        return changeToDto(readStatus);
    }

    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        return readStatuses.stream()
                .map(BasicReadStatusService::changeToDto)
                .collect(Collectors.toList());
    }

    public ReadStatusDto update(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId())
                .orElseThrow(() -> new ResourceNotFoundException("ReadStatus not found."));
        readStatus.update(request.lastReadTime());
        readStatusRepository.save(readStatus);
        return changeToDto(readStatus);
    }

    public void delete(UUID readStatusId) {
        validateReadStatus.validateReadStatus(readStatusId);
        readStatusRepository.delete(readStatusId);
    }

    private static ReadStatusDto changeToDto(ReadStatus readStatus) {
        return new ReadStatusDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime(), readStatus.getCreatedAt(), readStatus.getUpdatedAt());
    }
}
