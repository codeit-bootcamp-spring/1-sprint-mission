package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public ReadStatus create(ReadStatusCreateRequest request) {
        Channel channel = channelRepository.findById(request.channelId());
        if (channel == null) {
            throw new IllegalArgumentException("Channel not found.");
        }
        User user = userRepository.findByUserId(request.userId());
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        ReadStatus existingReadStatus = readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId());
        if (existingReadStatus != null) {
            throw new IllegalArgumentException("ReadStatus already exists for this user and channel.");
        }

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId());
        return readStatusRepository.save(readStatus);
    }

    public ReadStatusDto findById(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId);
        if (readStatus == null) {
            throw new IllegalArgumentException("ReadStatus not found.");
        }
        return new ReadStatusDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime());
    }

    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        return readStatuses.stream()
                .map(readStatus -> new ReadStatusDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime()))
                .toList();
    }

    public ReadStatus update(UUID readStatusId, ReadStatusCreateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId);
        if (readStatus == null) {
            throw new IllegalArgumentException("ReadStatus not found.");
        }

        // ReadStatus 수정
        readStatus.update(request.lastReadTime());
        return readStatusRepository.save(readStatus);
    }

    public void delete(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId);
        if (readStatus == null) {
            throw new IllegalArgumentException("ReadStatus not found.");
        }
        readStatusRepository.delete(readStatusId);
    }
}
