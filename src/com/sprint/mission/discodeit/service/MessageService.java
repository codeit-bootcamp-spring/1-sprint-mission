package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {
    void createMessage(Message message);

    Message readMessage(String msgID);

    List<Message> readAllMessage();
    public void modifyMessage (String msgID);
    public void deleteMessage (String msgID);


}
