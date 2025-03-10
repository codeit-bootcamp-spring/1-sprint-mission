package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
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
    public ReadStatusDTO.response create(ReadStatusDTO.request request) {
        ReadStatus status = new ReadStatus(request.userId(), request.channelId());
        repository.save(status);
        return new ReadStatusDTO.response(status.getId(), status.getUserId(), status.getChannelId(), status.getLastRead_at());
    }

    @Override
    public ReadStatusDTO.response markMessageAsRead(UUID messageId) {
        ReadStatus status = repository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("저장되지 않았거나, 삭제된 아이디입니다." + messageId));
//                .orElseThrow(() -> new IllegalArgumentException("Message Read Status not found"));
        status.markAsRead();
        repository.save(status);

        return new ReadStatusDTO.response(status.getId(), status.getUserId(), status.getChannelId(), status.getLastRead_at());
    }

    @Override
    public List<ReadStatusDTO.response> getUserMessageReadStatus(UUID userId, UUID channelId) {
        List<ReadStatus> statusList = repository.findByUserIdAndChannelId(userId,channelId);
        return statusList.stream()
                .map(status -> new ReadStatusDTO.response(status.getId(), status.getUserId(), status.getChannelId(), status.getLastRead_at()))
                .toList();
    }
}
