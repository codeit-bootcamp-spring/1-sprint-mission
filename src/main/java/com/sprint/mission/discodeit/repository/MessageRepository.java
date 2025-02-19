package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
     Message getMessage(UUID channelId, UUID messageId);
     LinkedHashMap<UUID, Message> getChannelMessagesMap(UUID channelId);
     boolean deleteMessage(UUID channelId, UUID messageId);
     boolean deleteChannelMessagesMap(UUID channelId);
     boolean saveMessage(UUID channelId, Message message);
     boolean isMessageExist(UUID channelId, UUID messageId);
     boolean addChannelMessagesMap(UUID channelId, LinkedHashMap<UUID, Message> messagesMap);
     Optional<Instant> getLastMessageCreatedAt(UUID channelId);
     //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
