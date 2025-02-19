package com.sprint.mission.discodeit.service.featureService;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus create(ReadStatusDTO readStatusDTO) {
        if(readStatusRepository.findAll().values().stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(readStatusDTO.channelId())
                        && readStatus.getUserId().equals(readStatusDTO.userId()))) {
            throw new IllegalStateException("UserId와 ChannelId가 이미 존재합니다.");
        }
        ReadStatus readStatus = new ReadStatus(readStatusDTO);
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus findById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(()-> new NoSuchElementException("찾을 수 없습니다."));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        Map<UUID,ReadStatus> readStatusMap = readStatusRepository.findAll();
        List<ReadStatus> readStatusList = readStatusMap.values().stream().filter(readStatus ->
                readStatus.getUserId().equals(userId)).collect(Collectors.toList());
        return readStatusList;
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusDTO readStatusDTO) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없습니다."));
        readStatus.update(readStatusDTO.lastActiveAt());
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }
}
