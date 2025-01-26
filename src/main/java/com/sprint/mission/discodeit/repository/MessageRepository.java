package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;

public interface MessageRepository {
    // 저장
    void save(Message message);

    // 불러오기
    Map<UUID, Message> load();

    // 삭제
    void delete(UUID id);
}
