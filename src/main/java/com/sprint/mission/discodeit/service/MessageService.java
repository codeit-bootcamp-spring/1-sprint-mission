package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    public abstract UUID createMessage(Channel channel, String messageText);

    // Read : 전체 메세지 조회, 특정 메세지 읽기
    public abstract int showAllMessages();
    public abstract Message getMessageById(UUID id);

    // Update : 특정 메세지 수정
    public abstract void updateMessageText(UUID id);

    // Delete : 전체 메세지 삭제, 특정 메세지 삭제
    public abstract void deleteAllMessages();
    public abstract void deleteMessageById(UUID id);
}
