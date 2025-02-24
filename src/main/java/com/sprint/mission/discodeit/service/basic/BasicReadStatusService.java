package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatusDTO.idResponse create(ReadStatusDTO.request readStatusReqDto) {
        try {
            Long statusId = readStatusRepository.save(new ReadStatus(readStatusReqDto));
            return ReadStatusDTO.idResponse.builder().id(statusId).uuid(readStatusRepository.load(statusId).getId()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReadStatusDTO.response find(Long id) {
        ReadStatus readStatus = readStatusRepository.load(id);
        return ReadStatusDTO.response.builder()
                .id(id)
                .uuid(readStatus.getId())
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .lastReadAt(readStatus.getLastReadAt())
                .build();
    }

    @Override
    public ReadStatusDTO.response find(UUID uuid) {
        Map.Entry<Long, ReadStatus> readStatus = readStatusRepository.load(uuid);
        return ReadStatusDTO.response.builder()
                .id(readStatus.getKey())
                .uuid(readStatus.getValue().getId())
                .userId(readStatus.getValue().getUserId())
                .channelId(readStatus.getValue().getChannelId())
                .lastReadAt(readStatus.getValue().getLastReadAt())
                .build();
    }

    @Override
    public List<ReadStatusDTO.response> findAllByUserId(UUID userId) {
        Map<Long, ReadStatus> allReadStatus = readStatusRepository.findAllByUserId(userId);
        return allReadStatus.entrySet().stream()
                .map(entry -> ReadStatusDTO.response.builder()
                        .id(entry.getKey())
                        .uuid(entry.getValue().getId())
                        .userId(entry.getValue().getUserId())
                        .channelId(entry.getValue().getChannelId())
                        .lastReadAt(entry.getValue().getLastReadAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatusDTO.idResponse update(ReadStatusDTO.update updateDTO) {
        ReadStatus readStatus = readStatusRepository.load(updateDTO.id());

        if (readStatus == null) {
            throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
        }

        if (updateDTO.lastReadAt() != null) {
            // 마지막에 읽은 것만 업데이트 하면 됨
            readStatus.updateLastReadAt(updateDTO.lastReadAt());
            readStatusRepository.update(updateDTO.id(), readStatus);
        }
        return ReadStatusDTO.idResponse.builder().id(updateDTO.id()).uuid(readStatus.getId()).build();
    }

    @Override
    public ReadStatusDTO.idResponse delete(Long id) {
        readStatusRepository.delete(id);
        return ReadStatusDTO.idResponse.builder().id(id).uuid(readStatusRepository.load(id).getId()).build();
    }

    @Override
    public ReadStatusDTO.idResponse delete(UUID uuid) {
        Map.Entry<Long, ReadStatus> readStatus = readStatusRepository.load(uuid);
        readStatusRepository.delete(readStatus.getKey());
        return ReadStatusDTO.idResponse.builder().id(readStatus.getKey()).uuid(readStatus.getValue().getId()).build();
    }
}
