package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message); //메시지 생성
    Message read(UUID id); // 메시지 단일 조회
    List<Message> readAll(); // 모든 메시지 조회
    void update(UUID id,Message message); // 메시지 수정
    void delete(UUID id); //메시지 삭제
}
