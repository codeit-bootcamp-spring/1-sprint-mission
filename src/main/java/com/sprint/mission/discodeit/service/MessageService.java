package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    // 메시지 생성
    Message createMessage(String username, UUID channelId, String content);

    // 메시지 단건 조회
    Message getMessageById(UUID messageId);

    // 메시지 다건 조회
    List<Message> getAllMessages();


    // 채널별 메시지 조회
    List<Message> getMessagesByChannel(UUID channelId);

    // 메시지 내용 수정
    Message updateMessageContent(UUID messageId, String newContent);

    // 메시지 삭제
    boolean deleteMessage(UUID messageId);
}
