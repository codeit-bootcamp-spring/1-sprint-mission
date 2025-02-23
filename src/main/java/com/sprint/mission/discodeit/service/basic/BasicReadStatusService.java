package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicReadStatusService implements ReadStatusService {
    private static final Logger log = LoggerFactory.getLogger(BasicReadStatusService.class);
    private final ReadStatusRepository readStatusRepository;

    public BasicReadStatusService(ReadStatusRepository readStatusRepository) {
        this.readStatusRepository = readStatusRepository;
    }

    @Override
    public ReadStatusDto createReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
        readStatusRepository.save(readStatus);
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt()
        );
    }

    @Override
    public ReadStatusDto findReadStatusById(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReadStatus not found with id: " + id));
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt()
        );
    }

    @Override
    public ReadStatusDto findLastReadMessage(UUID userId, UUID channelId) {
        ReadStatus readStatus = readStatusRepository.findAllByUserId(userId).stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No ReadStatus found for user and channel"));
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt()
        );
    }

    @Override
    public void updateLastReadAt(UUID id, Instant newTime) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReadStatus not found with id: " + id));
        readStatus.updateLastReadAt(newTime);
        readStatusRepository.save(readStatus);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }
}
