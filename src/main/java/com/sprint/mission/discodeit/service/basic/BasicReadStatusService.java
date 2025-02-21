package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;
//    private final ChannelService channelService;

    @Override
    public ReadStatusResponse create(ReadStatusRequest request) {
        User user = userService.findByIdOrThrow(request.userId());
//        Channel channel = channelService.findByIdOrThrow(channelId);
        if (readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
            throw new DuplicateRequestException("ReadStatus already exists");
        }
        ReadStatus newReadStatus = ReadStatus.createReadStatus(request.userId(), request.channelId());
        readStatusRepository.save(newReadStatus);
        log.info("Create Read Status : {}", newReadStatus);
        return ReadStatusResponse.EntityToDto(newReadStatus);
    }

    @Override
    public ReadStatusResponse findById(UUID id) {
        return ReadStatusResponse.EntityToDto(findByIdOrThrow(id));
    }

    @Override
    public ReadStatus findByIdOrThrow(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Read Status does not exist"));
    }

    @Override
    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllUserId(userId).stream()
                .map(ReadStatusResponse::EntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatusResponse> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllChannelId(channelId).stream()
                .map(ReadStatusResponse::EntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatusResponse update(UUID id) {
        ReadStatus readStatus = findByIdOrThrow(id);
        readStatus.updateUpdateAt();
        return ReadStatusResponse.EntityToDto(readStatusRepository.save(readStatus));
    }

    @Override
    public void deleteById(UUID id) {
        readStatusRepository.deleteById(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        readStatusRepository.deleteAllByChannelId(channelId);
    }
}
