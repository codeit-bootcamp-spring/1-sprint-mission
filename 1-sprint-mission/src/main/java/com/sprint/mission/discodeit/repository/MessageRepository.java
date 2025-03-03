package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;

public interface MessageRepository {
    public Map<UUID, Message> loadMessageTxt();
    public void saveMessageTxt();

}
