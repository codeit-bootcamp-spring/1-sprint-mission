package com.sprint.mission.discodeit.service.basic;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;
//    private final ChannelService channelService;

    @Override
    public ReadStatus create(UUID userId, UUID channelId) {
        User user = userService.findByIdOrThrow(userId);
//        Channel channel = channelService.findByIdOrThrow(channelId);
        if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
            throw new DuplicateRequestException("ReadStatus already exists");
        }
        ReadStatus newReadStatus = ReadStatus.createReadStatus(userId, channelId);
        readStatusRepository.save(newReadStatus);
        log.info("Create Read Status : {}", newReadStatus);
        return newReadStatus;
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Read Status does not exist"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllUserId(userId);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllChannelId(channelId);
    }

    @Override
    public ReadStatus update(UUID id) {
        ReadStatus readStatus = findById(id);
        readStatus.updateUpdateAt();
        return readStatusRepository.save(readStatus);
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
