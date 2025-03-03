package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusReadResponse;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("fileReadStatusService")
@RequiredArgsConstructor
public class FileReadStatusService implements ReadStatusService {

    private final Map<UUID, ReadStatusReadResponse> readStatusStorage = new HashMap<>();

    @Override
    public void create(ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatusReadResponse readStatus = new ReadStatusReadResponse(
                UUID.randomUUID(),
                readStatusCreateRequest.getUserId(),
                readStatusCreateRequest.getMessageId(),
                readStatusCreateRequest.getReadAt()
        );
        readStatusStorage.put(readStatus.getId(), readStatus);
    }

    @Override
    public void update(UUID id, ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatusReadResponse readStatus = readStatusStorage.get(id);
        if (readStatus != null) {
            readStatus.setReadAt(readStatusUpdateRequest.getReadAt());
        }
    }

    @Override
    public void delete(UUID id) {
        readStatusStorage.remove(id);
    }

    // ✅ `readByUserId(UUID userId)` 메서드 구현 (오류 해결)
    @Override
    public List<ReadStatusReadResponse> readByUserId(UUID userId) {
        List<ReadStatusReadResponse> result = new ArrayList<>();
        for (ReadStatusReadResponse readStatus : readStatusStorage.values()) {
            if (readStatus.getUserId().equals(userId)) {
                result.add(readStatus);
            }
        }
        return result;
    }
}
