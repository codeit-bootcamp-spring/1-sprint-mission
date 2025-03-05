package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.util.EntryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;

    @Override
    public CommonDTO.idResponse create(ReadStatusDTO.request readStatusReqDto) {
        try {
            Long statusId = readStatusRepository.save(new ReadStatus(readStatusReqDto.userId(), readStatusReqDto.channelId(), readStatusReqDto.lastReadAt()));
            return CommonDTO.idResponse.from(statusId, readStatusRepository.load(statusId).getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReadStatusDTO.response find(Long id) {
        ReadStatus readStatus = readStatusRepository.load(id);
        return ReadStatusDTO.response.from(EntryUtils.of(id, readStatus));
    }

    @Override
    public ReadStatusDTO.response find(UUID uuid) {
        Map.Entry<Long, ReadStatus> readStatus = readStatusRepository.load(uuid);
        return ReadStatusDTO.response.from(readStatus);
    }

    @Override
    public List<ReadStatusDTO.response> findAllByUserId(UUID userId) {
        Map<Long, ReadStatus> allReadStatus = readStatusRepository.findAllByUserId(userId);
        return allReadStatus.entrySet().stream()
                .map(ReadStatusDTO.response::from)
                .collect(Collectors.toList());
    }

    @Override
    public CommonDTO.idResponse update(Long readStatusId, Instant lastReadAt) {
        ReadStatusDTO.update updateDTO = ReadStatusDTO.update.of(readStatusId, lastReadAt);
        ReadStatus readStatus = readStatusRepository.load(updateDTO.id());

        if (readStatus == null) {
            throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
        }

        if (updateDTO.lastReadAt() != null) {
            // 마지막에 읽은 것만 업데이트 하면 됨
            readStatus.updateLastReadAt(updateDTO.lastReadAt());
            readStatusRepository.update(updateDTO.id(), readStatus);
        }
        return CommonDTO.idResponse.from(updateDTO.id(), readStatus.getId());
    }

    @Override
    public CommonDTO.idResponse delete(Long id) {
        readStatusRepository.delete(id);
        return CommonDTO.idResponse.from(id, readStatusRepository.load(id).getId());
    }

    @Override
    public CommonDTO.idResponse delete(UUID uuid) {
        Map.Entry<Long, ReadStatus> readStatus = readStatusRepository.load(uuid);
        readStatusRepository.delete(readStatus.getKey());
        return CommonDTO.idResponse.from(readStatus.getKey(), uuid);
    }
}
