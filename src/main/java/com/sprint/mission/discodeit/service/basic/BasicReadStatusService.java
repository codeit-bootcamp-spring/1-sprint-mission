package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class BasicReadStatusService implements ReadStatusService {
    private static final Logger log = LoggerFactory.getLogger(BasicReadStatusService.class);
    private final ReadStatusRepository readStatusRepository;

    public BasicReadStatusService(ReadStatusRepository readStatusRepository) {
        this.readStatusRepository = readStatusRepository;
    }

    @Override
    public ReadStatusDto createReadStatus(UUID userId, UUID channelId) {
        ReadStatus readStatus = new ReadStatus(userId, channelId, Instant.now());
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
                .orElseThrow(() -> new RuntimeException("해당 ID로 ReadStatus를 찾을 수 없습니다: " + id));
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
                .orElseThrow(() -> new RuntimeException("해당 사용자 및 채널에 대한 ReadStatus가 없습니다."));
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
                .orElseThrow(() -> new RuntimeException("해당 ID로 ReadStatus를 찾을 수 없습니다: " + id));
        readStatus.updateLastReadAt(newTime);
        readStatusRepository.save(readStatus);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }
}
