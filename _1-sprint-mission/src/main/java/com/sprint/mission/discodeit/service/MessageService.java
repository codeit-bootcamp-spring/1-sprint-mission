package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID channelId, UUID authorId); //메시지 생성
    Message find(UUID messageId); // 메시지 단일 조회
    List<Message> findAll(); // 모든 메시지 조회
    Message update(UUID messageId, String newContent); // 메시지 수정
    void delete(UUID messageId); //메시지 삭제
}
