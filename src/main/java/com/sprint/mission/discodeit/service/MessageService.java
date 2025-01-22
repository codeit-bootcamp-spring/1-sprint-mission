package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageService {
    // 생성
    void craete(UUID channelId, String message, UUID writerId);
    
    // 읽기
    Message read(UUID id);
    
    // 모두 읽기
    List<Message> allRead();
    List<Message> allRead(UUID channelId);

    // 수정
    void updateMessage(UUID messageId, String updateMessage);
    
    // 삭제
    void delete(UUID messageId);
}