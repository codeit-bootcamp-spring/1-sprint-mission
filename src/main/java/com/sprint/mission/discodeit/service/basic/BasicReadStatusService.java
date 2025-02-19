package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResponse create(ReadStatusCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        if (readStatusRepository.existsByUserAndChannel(user, channel)) {
            throw new IllegalArgumentException("ReadStatus already exists for this user and channel.");
        }

        ReadStatus readStatus = new ReadStatus(user, channel, request.getLastReadAt());
        readStatusRepository.save(readStatus);

        return new ReadStatusResponse(readStatus);
    }

    @Override
    public ReadStatusResponse find(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));
        return new ReadStatusResponse(readStatus);
    }

    @Override
    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findByUserId(userId);
        return readStatuses.stream().map(ReadStatusResponse::new).collect(Collectors.toList());
    }

    @Override
    public ReadStatusResponse update(ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));

        readStatus.updateReadTime(request.getLastReadAt());
        readStatusRepository.save(readStatus);

        return new ReadStatusResponse(readStatus);
    }


    @Override
    public void delete(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new NoSuchElementException("ReadStatus not found");
        }
        readStatusRepository.deleteById(id);
    }
}
