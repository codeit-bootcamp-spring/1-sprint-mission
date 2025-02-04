package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void create(Message message); // 메시지 생성
    Optional<Message> read(UUID id); // 특정 메시지 조회
    List<Message> readAll(); // 모든 메시지 조회
    void update(UUID id, Message message); // 메시지 업데이트
    void delete(UUID id); // 메시지 삭제
}
