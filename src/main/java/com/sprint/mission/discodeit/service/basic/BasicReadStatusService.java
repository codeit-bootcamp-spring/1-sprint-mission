package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.web.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    @Override
    public ReadStatus create(ReadStatus readStatus) {
        if (readStatus.getUserId() == null || readStatus.getChannelId() == null) {
            throw new IllegalArgumentException("해당 채널 혹은 유저가 존재하지 않습니다.");
        }
        if(!readStatusRepository.findAllByUserId(readStatus.getUserId()).isEmpty()) {
            if(!readStatusRepository.findAllByChannelId(readStatus.getChannelId()).isEmpty()){
                throw new IllegalArgumentException("해당 채널과 유저가 이미 존재합니다.");
            }
        }
        return readStatusRepository.createReadStatus(readStatus);
    }

    @Override
    public Optional<ReadStatus> find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateDto readStatusUpdate) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(()->new IllegalArgumentException("해당 ID의 ReadStatus가 존재하지 않습니다."));
        readStatus.updateRead(readStatusUpdate.getNewLastReadAt());
        return readStatus;
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
    }
}
