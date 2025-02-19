package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ReadStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusValidator readStatusValidator;

    @Override
    public UUID create(ReadStatusCreateDTO dto) {
        readStatusValidator.validateReadStatus(dto.getUserId(), dto.getChannelId());
        ReadStatus readStatus = new ReadStatus(dto.getUserId(), dto.getChannelId());
        readStatusRepository.save(readStatus);
        return readStatus.getId();
    }

    @Override
    public ReadStatus find(UUID id) {
        ReadStatus findReadStatus = readStatusRepository.findOne(id);
        Optional.ofNullable(findReadStatus)
                .orElseThrow(() -> new NotFoundException(ErrorCode.READ_STATUS_NOT_FOUND));
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
    public ReadStatus update(UUID id, ReadStatusUpdateDTO dto) {
        ReadStatus findReadStatus = readStatusRepository.findOne(id);
        findReadStatus.updateReadStatus(dto.getTime());
        readStatusRepository.update(findReadStatus);
        return findReadStatus;
    }

    @Override
    public UUID delete(UUID id) {
        return readStatusRepository.delete(id);
    }

}
