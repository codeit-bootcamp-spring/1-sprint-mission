package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusReadResponse;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void create(ReadStatusCreateRequest readStatusCreateRequest);
    void update(UUID id, ReadStatusUpdateRequest readStatusUpdateRequest);
    void delete(UUID id);

    // ✅ 특정 사용자의 메시지 수신 정보 조회
    List<ReadStatusReadResponse> readByUserId(UUID userId);
}
