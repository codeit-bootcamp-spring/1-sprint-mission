package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);  // 메시지 저장

    Optional<Message> findById(UUID id);  // 특정 메시지 조회

    List<Message> findAll();  // 모든 메시지 조회

    void update(UUID id, Message message);  // 메시지 수정

    void delete(UUID id);  // 메시지 삭제
}
