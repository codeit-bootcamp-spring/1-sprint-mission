package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("basicReadStatusService")
@Primary
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;  // ✅ fileReadStatusRepository가 기본적으로 주입됨

    @Override
    public void markAsRead(UUID userId, UUID channelId, Instant timestamp) {
        Optional<ReadStatus> existingStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId);
        if (existingStatus.isPresent()) {
            ReadStatus status = existingStatus.get();
            status.setReadAt(timestamp);
            readStatusRepository.save(status);
        } else {
            readStatusRepository.save(new ReadStatus(userId, channelId, timestamp));
        }
    }

    @Override
    public Optional<ReadStatus> getLastRead(UUID userId, UUID channelId) {
        return readStatusRepository.findByUserIdAndChannelId(userId, channelId);
    }

    @Override
    public List<ReadStatus> getUserReadStatuses(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public List<ReadStatus> getChannelReadStatuses(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        readStatusRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusRepository.deleteByChannelId(channelId);
    }
}
