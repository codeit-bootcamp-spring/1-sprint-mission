package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.repository.jcf.JCF_Message;
import com.sprint.mission.discodeit.service.jcf.JCF_message;
import java.util.List;
import java.util.UUID;

public interface UserService {

    void creat(String name);

    void delete(UUID userId, JCF_message jcfMessage);

    //여기아님
    String getName(UUID userId);

    void update(UUID userId, String name);

    void addMessage(UUID messageId, UUID userId);

    void addChannel(UUID channelId, UUID userId);

    UUID write(String name);
}
