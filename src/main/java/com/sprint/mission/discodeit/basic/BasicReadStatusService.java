package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.ReadStatusType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final FileUserRepository fileUserRepository;


    @Override
    public ReadStatusDTO create(ReadStatusDTO readStatusDTO) {
        ReadStatus readStatus = new ReadStatus(readStatusDTO.getUserId(), readStatusDTO.getChannelId(), Instant.now());
        readStatusRepository.save(readStatus);
        return new ReadStatusDTO(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime());
    }

    @Override
    public ReadStatusDTO find(String userId, String channelId) {
        ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
        return new ReadStatusDTO(readStatus.getUserId(), readStatus.getChannelId(), readStatus.getLastReadTime());
    }

    @Override
    public void delete(String userId, String channelId) {
        readStatusRepository.deleteByUserIdAndChannelId(userId, channelId);
    }

    @Override
    public ReadStatusType readOnOff(String userId) {
        boolean userExists = userRepository.findById(userId).isPresent();
        boolean channelExists = channelRepository.findById(userId).isPresent();

        if (userExists != channelExists) {
            return ReadStatusType.NOT_READ;
        } else {
            return ReadStatusType.READ;
        }
    }
}