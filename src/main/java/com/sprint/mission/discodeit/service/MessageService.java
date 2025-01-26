package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.service.jcf.JCF_user;
import java.util.List;
import java.util.UUID;

public interface MessageService {
<<<<<<< HEAD
=======
    public void creat(String content, UUID userId, UUID channelId);
>>>>>>> 5206eed33a9c5cfc572bc8a1095473360a1463b3

    UUID creat(UUID userId, String content, UUID channelId);

    void delete(UUID messageId);

<<<<<<< HEAD
    void update(UUID messageId, String updateMessage);
=======
    List<Message> write(UUID userId, UUID channelId);
>>>>>>> 5206eed33a9c5cfc572bc8a1095473360a1463b3

    void DeleteMessageList(List<UUID> deleteMessageList);

    //여기아님
    List<String> getMessageList(List<UUID> messageIdList, JCF_user jcfUser);

}
