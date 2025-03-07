package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        if (readStatusRepository.findAllByUserId(userId).stream()
                .anyMatch(rs -> rs.getChannel().getId().equals(channelId))) {
            throw new BadRequestException("ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
        }

        // 요청으로 받은 Instant를 Timestamp로 변환 (null 체크 포함)
        Timestamp lastReadAtTimestamp = request.lastReadAt() != null ? Timestamp.from(request.lastReadAt()) : null;
        Timestamp now = Timestamp.from(Instant.now());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel with id " + channelId + " not found"));

        ReadStatus readStatus = ReadStatus.builder()
                .user(user)
                .channel(channel)
                .lastReadAt(lastReadAtTimestamp)
                .createdAt(now)
                .updatedAt(now)
                .build();

        ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

        Instant lastReadInstant = savedReadStatus.getLastReadAt() != null
                ? savedReadStatus.getLastReadAt().toInstant()
                : null;

        return ReadStatusDto.builder()
                .id(savedReadStatus.getId())
                .userId(savedReadStatus.getUser().getId())
                .channelId(savedReadStatus.getChannel().getId())
                .lastReadAt(lastReadInstant)
                .build();
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("ReadStatus with id " + readStatusId + " not found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
        Timestamp newLastReadAtTimestamp = Timestamp.from(request.newLastReadAt());
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("ReadStatus with id " + readStatusId + " not found"));
        readStatus.update(newLastReadAtTimestamp);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw new ResourceNotFoundException("ReadStatus with id " + readStatusId + " not found");
        }
        readStatusRepository.deleteById(readStatusId);
    }
}