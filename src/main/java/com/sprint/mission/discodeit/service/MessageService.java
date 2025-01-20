package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface MessageService {
    void createMessage(Message message);

    Message readMessage(String id);

    void updateMessage(Message message);

    void deleteMessage(String id);

    List<Message> readAllMessages(); // 모든 사용자 반환

}
