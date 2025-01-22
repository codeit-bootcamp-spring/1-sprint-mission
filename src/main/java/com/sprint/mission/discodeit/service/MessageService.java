package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.Collection;
import java.util.UUID;

public interface MessageService {
    UUID createMessage(Channel channel, String messageText);

    // Read : 전체 메세지 조회, 특정 메세지 읽기
    Collection<Message> showAllMessages();
    Message getMessageById(UUID id);

    // Update : 특정 메세지 수정
    void updateMessageText(UUID id);

    // Delete : 전체 메세지 삭제, 특정 메세지 삭제
    void deleteAllMessages();
    void deleteMessageById(UUID id);
}
