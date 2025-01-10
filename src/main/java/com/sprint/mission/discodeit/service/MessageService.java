package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.log.MyLog;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MyLog<Message> createMessage(String content, User fromUser, User toUser);

    MyLog<List<Message>> getMessageByUser(User fromUser, User toUser);

    MyLog<Message> updateMessageId(UUID id, String newContent);

    List<Message> getAllMessages();

    MyLog<Message> deleteMessage(Message message);

}
