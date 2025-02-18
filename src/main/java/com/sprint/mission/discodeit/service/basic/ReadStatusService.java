package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatus create(ReadStatusDto dto) {
        UUID userId = dto.userId();
        UUID channelId = dto.channelId();

        userRepository.findById(userId);
        channelRepository.findById(channelId);

        List<ReadStatus> readStatuses = readStatusRepository.findByUserId(userId);
        for (ReadStatus readStatus : readStatuses) {
            if (readStatus.getChannelId().equals(channelId)) {
                throw new DuplicateException("이미 존재하는 ReadStatus");
            }
        }

        return readStatusRepository.save(ReadStatus.of(userId, channelId));
    }

    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id);
    }

    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId);
    }

    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }
}