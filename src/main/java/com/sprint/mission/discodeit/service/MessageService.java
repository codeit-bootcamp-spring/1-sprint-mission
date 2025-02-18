package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 생성
    void craete(Message message);

    // 읽기
    Message read(UUID id);

    // 모두 읽기
    List<Message> readAll();
    List<Message> readAll(UUID channelId, ChannelService channelService);

    // 수정
    void updateContext(UUID id, String updateContext);

    // 삭제
    void delete(UUID id);

    // 메시지 존재 여부 확인
    void messageIsExist(UUID id);
}