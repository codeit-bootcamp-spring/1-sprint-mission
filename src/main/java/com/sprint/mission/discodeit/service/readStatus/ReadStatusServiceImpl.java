package com.sprint.mission.discodeit.service.readStatus;


import com.sprint.mission.discodeit.dto.ReadStatusService.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.ReadStatusService.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Qualifier("BasicReadStatusService")
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequestDTO request) {
        if(!userRepository.existsById((request.userId()))) {
            throw new NoSuchElementException("User not found");
        }

        if(!channelRepository.existsById((request.channelId()))) {
            throw new NoSuchElementException("Channel not found");
        }

        if(readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
            throw new IllegalArgumentException("ReadStatus already exists");
        }

        ReadStatus readStatus = request.from();

        return readStatusRepository.save(readStatus);
    }
    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequestDTO request) {
        ReadStatus readStatus = readStatusRepository.findById(request.readStatusId())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id not found"));

        if(request.lastReadAt() == null) {
            readStatus.updateLastRead(request.lastReadAt());
        }

        return readStatusRepository.save(readStatus);
    }
    @Override
    public void delete(UUID readStatusId) {

        if (!userRepository.existsById((readStatusId))) {
            throw new NoSuchElementException("User not found");
        }

        readStatusRepository.deleteById(readStatusId);
    }





}
