package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.ReadStatusReadDTO;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void create(ReadStatusCreateDTO readStatusCreateDTO);
    void update(UUID id, ReadStatusUpdateDTO readStatusUpdateDTO);
    void delete(UUID id);

    // ✅ 특정 사용자의 메시지 수신 정보 조회
    List<ReadStatusReadDTO> readByUserId(UUID userId);
}
