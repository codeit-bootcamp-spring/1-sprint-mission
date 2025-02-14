package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

public interface MessageRepository {
     Message getMessage(UUID channelId, UUID messageId) throws Exception;
     LinkedHashMap<UUID, Message> getChannelMessagesMap(UUID channelId) throws Exception;
     boolean deleteMessage(UUID channelId, UUID messageId) throws Exception;
     boolean saveMessage(UUID channelId, Message message) throws Exception;
     boolean isMessageExist(UUID channelId, UUID messageId) throws Exception;
     boolean addChannelMessagesMap(UUID channelId, LinkedHashMap<UUID, Message> messagesMap) throws Exception;
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
