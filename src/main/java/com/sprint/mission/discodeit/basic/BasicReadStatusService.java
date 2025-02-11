package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatusDTO create(ReadStatusDTO readStatusDTO) {
        ReadStatus readStatus = new ReadStatus(readStatusDTO.getUserId(), readStatusDTO.getChannelId(), Instant.now());
        readStatusRepository.save(readStatus);
        return new ReadStatusDTO(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime());
    }

    @Override
    public ReadStatusDTO find(String userId, String channelId) {
        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
        return new ReadStatusDTO(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime());
    }

    @Override
    public void delete(String userId, String channelId) {
        readStatusRepository.deleteByUserIdAndChannelId(userId, channelId);
    }
}