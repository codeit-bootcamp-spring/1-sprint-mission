package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.repository.jcf.JCF_Message;
import com.sprint.mission.discodeit.service.jcf.JCF_message;
import com.sprint.mission.discodeit.service.jcf.JCF_user;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

    void creat(String title);

    void addUser(UUID userId, UUID channelId, JCF_user jcfUser);

    void addMessage(String messageContent, UUID channelId, UUID userId,
        JCF_message jcfMessage, JCF_user jcfUser);

    void delete(UUID channelId, JCF_message jcfMessage);

    //여기아님
    List<UUID> getUserIdList(UUID channelId);

    //여기아님
    List<UUID> getMessageIdList(UUID channelId);

    //여기아님
    List<String> getMessageList(UUID channelId, JCF_message jcfMessage, JCF_user jcfUser);

    void update(UUID channelId, String title);

    UUID write(String title);
}
