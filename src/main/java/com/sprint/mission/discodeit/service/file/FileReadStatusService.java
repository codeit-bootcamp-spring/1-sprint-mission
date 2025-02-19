package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.ReadStatusReadDTO;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("fileReadStatusService")
@RequiredArgsConstructor
public class FileReadStatusService implements ReadStatusService {

    private final Map<UUID, ReadStatusReadDTO> readStatusStorage = new HashMap<>();

    @Override
    public void create(ReadStatusCreateDTO readStatusCreateDTO) {
        ReadStatusReadDTO readStatus = new ReadStatusReadDTO(
                UUID.randomUUID(),
                readStatusCreateDTO.getUserId(),
                readStatusCreateDTO.getMessageId(),
                readStatusCreateDTO.getReadAt()
        );
        readStatusStorage.put(readStatus.getId(), readStatus);
    }

    @Override
    public void update(UUID id, ReadStatusUpdateDTO readStatusUpdateDTO) {
        ReadStatusReadDTO readStatus = readStatusStorage.get(id);
        if (readStatus != null) {
            readStatus.setReadAt(readStatusUpdateDTO.getReadAt());
        }
    }

    @Override
    public void delete(UUID id) {
        readStatusStorage.remove(id);
    }

    // ✅ `readByUserId(UUID userId)` 메서드 구현 (오류 해결)
    @Override
    public List<ReadStatusReadDTO> readByUserId(UUID userId) {
        List<ReadStatusReadDTO> result = new ArrayList<>();
        for (ReadStatusReadDTO readStatus : readStatusStorage.values()) {
            if (readStatus.getUserId().equals(userId)) {
                result.add(readStatus);
            }
        }
        return result;
    }
}
