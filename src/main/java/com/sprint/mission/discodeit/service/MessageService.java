package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.service.jcf.JCF_user;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    UUID creat(UUID userId, String content, UUID channelId);

    void delete(UUID messageId);

    void update(UUID messageId, String updateMessage);

    void DeleteMessageList(List<UUID> deleteMessageList);

    //여기아님
    List<String> getMessageList(List<UUID> messageIdList, JCF_user jcfUser);

}
