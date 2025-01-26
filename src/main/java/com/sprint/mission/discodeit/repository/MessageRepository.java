package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message); // 메시지 저장
    Message findById(UUID id); // ID로 메시지 찾기
    List<Message> findAll(); // 모든 메시지 찾기
    void update(UUID id, Message message); // 메시지 업데이트
    void delete(UUID id); // 메시지 삭제
}
