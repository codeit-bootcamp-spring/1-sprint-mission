package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusReadResponse;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("basicReadStatusService")
@Primary
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

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

    // ✅ `delete(UUID id)` 메서드 추가 (오류 해결)
    @Override
    public void delete(UUID id) {
        readStatusStorage.remove(id);
    }

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
