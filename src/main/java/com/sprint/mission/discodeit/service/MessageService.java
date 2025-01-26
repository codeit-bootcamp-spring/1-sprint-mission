package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageService {
    void createMessage(Message message);
    //HashSet<Message> readEveryMessageOfUser(UUID userId);
   //HashSet<Message> readChannelMessage(UUID channelId);
    void updateMessageById(UUID messageId, String contents);
    void deleteMessage(UUID messageId);
    List<Message> getMessageList();
    void setMessageList(List<Message> messageList);


}
