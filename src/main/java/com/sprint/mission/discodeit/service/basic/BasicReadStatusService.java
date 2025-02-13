package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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

    @Override
    public UUID create(ReadStatusCreateDTO dto) {
        checkDuplicateReadStatus(dto.getUserId(), dto.getChannelId());

        ReadStatus readStatus = new ReadStatus(dto.getUserId(), dto.getChannelId());
        return readStatus.getId();
    }

    @Override
    public ReadStatus find(UUID id) {
        ReadStatus findReadStatus = readStatusRepository.findOne(id);
        Optional.ofNullable(findReadStatus)
                .orElseThrow(() -> new NoSuchElementException("해당하는 findReadStatus 가 없습니다."));
        return findReadStatus;
    }

    @Override
    public List<ReadStatus> findAll(){
        return readStatusRepository.findAll();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(ReadStatusUpdateDTO readStatusUpdateDTO) {
        ReadStatus findReadStatus = readStatusRepository.findOne(readStatusUpdateDTO.getId());
        findReadStatus.updateReadStatus(readStatusUpdateDTO.getTime());
        readStatusRepository.update(findReadStatus);
        return findReadStatus;
    }

    @Override
    public UUID delete(UUID id) {
        return readStatusRepository.delete(id);
    }

    private void checkDuplicateReadStatus(UUID userId, UUID channelId) {
        if (readStatusRepository.findByUserIdAndChannlId(userId, channelId).isPresent()) {
            throw new IllegalArgumentException("중복된 ReadStatus 가 존재합니다. userid: " + userId+" channelId :"+channelId);
        }

    }
}
