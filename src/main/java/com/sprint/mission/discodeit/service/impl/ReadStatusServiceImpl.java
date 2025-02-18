package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReadStatusServiceImpl implements ReadStatusService {

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusDTO.CreateReadStatusDTO createReadStatusDTO) {
        // User와 Channel의 존재 여부 확인
        if (!userRepository.existsById(createReadStatusDTO.getUserId())) {
            throw new RuntimeException("User not found");
        }
        if (!channelRepository.existsById(createReadStatusDTO.getChannelId())) {
            throw new RuntimeException("Channel not found");
        }

        // 이미 존재하는 ReadStatus 확인
        ReadStatus existingReadStatus = readStatusRepository.findByUserIdAndChannelId(
                createReadStatusDTO.getUserId(), createReadStatusDTO.getChannelId());
        if (existingReadStatus != null) {
            throw new RuntimeException("ReadStatus already exists for this user and channel");
        }

        // 새로운 ReadStatus 생성
        ReadStatus readStatus = new ReadStatus();
        readStatus.setUserId(createReadStatusDTO.getUserId());
        readStatus.setChannelId(createReadStatusDTO.getChannelId());
        readStatus.setLastReadAt(createReadStatusDTO.getLastReadAt());
        readStatus.setCreatedAt(java.time.Instant.now());
        readStatus.setUpdatedAt(java.time.Instant.now());

        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReadStatus not found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID id, ReadStatusDTO.UpdateReadStatusDTO updateReadStatusDTO) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReadStatus not found"));

        // ReadStatus 내용 업데이트
        readStatus.setLastReadAt(updateReadStatusDTO.getNewLastReadAt());
        readStatus.setUpdatedAt(java.time.Instant.now());

        return readStatusRepository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReadStatus not found"));

        // ReadStatus 삭제
        readStatusRepository.delete(readStatus);
    }
}
