package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus create(CreateReadStatusRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("User not found");
        }
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("Channel not found");
        }
        if (readStatusRepository.findAllByUserId(request.userId()).stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(request.channelId()))) {
            throw new IllegalArgumentException("Read status already exists");
        }
        Instant lastReadTime = request.lastReadTime();
        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), lastReadTime);

        return readStatusRepository.save(readStatus);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public synchronized void updateReadStatus(UUID userId, UUID channelId) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");

        user.getReadStatuses().compute(channelId, (key, status) -> {
            if (status == null) return new ReadStatus(userId, channelId, Instant.now());
            return status;
        });

        userRepository.save(user);
    }
}
