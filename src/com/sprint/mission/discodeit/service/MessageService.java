package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {
    void createMessage(Message message);
    Message readMessage(String msgID);
    List<Message> readAllMessage();
    void modifyMessage(String msgID, String content);
    public void deleteMessage (String msgID);


}
