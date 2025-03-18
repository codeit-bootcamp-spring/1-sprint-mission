package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.dto.response.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository repository;

    @Override
    public ReadStatusResponse create(ReadStatusRequest request) {
        ReadStatus status = ReadStatusMapper.INSTANCE.toEntity(request);
        repository.save(status);
        return ReadStatusMapper.INSTANCE.toDto(status);
    }

    @Override
    public ReadStatusResponse markMessageAsRead(UUID messageId) {
        ReadStatus status = repository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다." + messageId));
//                .orElseThrow(() -> new IllegalArgumentException("Message Read Status not found"));
        status.markAsRead();
        repository.save(status);

        return ReadStatusMapper.INSTANCE.toDto(status);
    }

    @Override
    public List<ReadStatusResponse> getUserMessageReadStatus(UUID userId, UUID channelId) {
        List<ReadStatus> statusList = repository.findByUserIdAndChannelId(userId,channelId);
        return statusList.stream()
                .map(status -> ReadStatusMapper.INSTANCE.toDto(status))
                .toList();
    }
}
