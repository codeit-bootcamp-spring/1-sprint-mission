package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    //새로운 메시지 생성
    void createMessage(Message message);
    // 메세지 모두 조회
    List<Message> getAllMessageList();

    // message 고유 ID로 메세지 조회
    Message searchById(UUID messageId);

    // 메세지 삭제
    void deleteMessage(UUID messageId);

    // 메세지 수정
    void updateMessage(UUID messageId, String content);
}