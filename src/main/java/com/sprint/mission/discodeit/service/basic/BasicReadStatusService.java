package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest) {
        if (!channelRepository.existsById(readStatusCreateRequest.channelId())) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
        }
        if (!userRepository.existsById(readStatusCreateRequest.userId())) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }

        findAllByUserId(readStatusCreateRequest.userId())
                .forEach(readStatus -> {
                    if (readStatus.isSameChannelId(readStatusCreateRequest.channelId())) {
                        throw new IllegalArgumentException("[ERROR] 이미 존재하는 데이터입니다.");
                    }
                });

        return readStatusRepository.save(new ReadStatus(readStatusCreateRequest.channelId(), readStatusCreateRequest.userId()));
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return Optional.ofNullable(readStatusRepository.find(readStatusId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다."));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = find(readStatusId);
        readStatus.update(readStatusUpdateRequest.lastReadAt());
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다.");
        }
        readStatusRepository.delete(readStatusId);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);
        readStatuses.forEach(readStatus -> readStatusRepository.delete(readStatus.getId()));
    }
}
