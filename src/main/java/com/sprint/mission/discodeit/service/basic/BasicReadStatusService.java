package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
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
        User user = userRepository.findById(readStatusCreateRequest.userId())
            .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));

        Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
            .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        findAllByUserId(readStatusCreateRequest.userId())
                .forEach(readStatus -> {
                    if (readStatus.isSameChannelById(readStatusCreateRequest.channelId())) {
                        throw new IllegalArgumentException("[ERROR] 이미 존재하는 데이터입니다.");
                    }
                });

        return readStatusRepository.save(new ReadStatus(user, channel, readStatusCreateRequest.lastReadAt()));
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다."));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = find(readStatusId);
        readStatus.update(readStatusUpdateRequest.newLastReadAt());
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다.");
        }
        readStatusRepository.deleteById(readStatusId);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channelId);
        readStatuses.forEach(readStatus -> readStatusRepository.deleteById(readStatus.getId()));
    }
}
