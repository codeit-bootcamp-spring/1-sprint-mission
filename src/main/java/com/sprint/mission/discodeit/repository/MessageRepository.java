package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.Message;

import java.util.HashMap;
import java.util.UUID;

public interface MessageRepository {
    public Message getMessage(UUID messageId);
    public HashMap<UUID, Message> getMessagesMap();
    public boolean deleteMessage(UUID messageId);
    public boolean addMessage(Message message);
    public boolean isMessageExist(UUID messageId);
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
