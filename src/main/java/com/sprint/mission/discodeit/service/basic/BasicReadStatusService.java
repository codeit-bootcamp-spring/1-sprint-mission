package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public ReadStatusDto create(ReadStatusCreateRequest request) {
        Optional<Channel> channel = channelRepository.findById(request.channelId());
        if (channel.isEmpty()) {
            throw new IllegalArgumentException("Channel not found.");
        }
        Optional<User> user = userRepository.findByUserId(request.userId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        Optional<ReadStatus> existingReadStatus = readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId());
        if (existingReadStatus.isPresent()) {
            throw new IllegalArgumentException("ReadStatus already exists for this user and channel.");
        }

        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId());
        readStatusRepository.save(readStatus);
        return changeToDto(readStatus);
    }

    public ReadStatusDto findById(UUID readStatusId) {
        Optional<ReadStatus> readStatus = readStatusRepository.findById(readStatusId);
        if (readStatus.isEmpty()) {
            throw new IllegalArgumentException("ReadStatus not found.");
        }
        return changeToDto(readStatus.orElse(null));
    }

    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        return readStatuses.stream()
                .map(readStatus -> changeToDto(readStatus))
                .toList();
    }

    public ReadStatusDto update(ReadStatusUpdateRequest request) {

        Optional<ReadStatus> readStatus = readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId());
        if (readStatus.isEmpty()) {
            throw new IllegalArgumentException("ReadStatus not found.");
        }

        // ReadStatus 수정
        readStatus.get().update(request.lastReadTime());
        readStatusRepository.save(readStatus.orElse(null));
        return changeToDto(readStatus.orElse(null));
    }

    public void delete(UUID readStatusId) {
        Optional<ReadStatus> readStatus = readStatusRepository.findById(readStatusId);
        if (readStatus.isEmpty()) {
            throw new IllegalArgumentException("ReadStatus not found.");
        }
        readStatusRepository.delete(readStatusId);
    }

    private static ReadStatusDto changeToDto(ReadStatus readStatus) {
        return new ReadStatusDto(readStatus.getId(), readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime(), readStatus.getCreatedAt(), readStatus.getUpdatedAt());
    }
}
