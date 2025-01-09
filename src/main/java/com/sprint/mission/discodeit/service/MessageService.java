package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ChatBehavior;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Message createMessage(Message message, ChatBehavior chatBehavior);
    Optional<Message> getMessageById(String messageUUID, ChatBehavior chatBehavior);
    List<Message> getMessagesByChannel(String channelUUID);
    List<Message> getMessagesByThread(String threadUUID);
    Message updateMessage(String messageUUID, String newContent, String newContentImageUrl, ChatBehavior chatBehavior);
    boolean deleteMessage(String messageUUID, ChatBehavior chatBehavior);
    void addReactionToMessage(String messageUUID, String userUUID, String reactionType);
    void removeReactionFromMessage(String messageUUID, String userUUID);
    List<Message> getChatHistory(ChatBehavior chatBehavior);
    void modifyMessage(String messageId, String content, ChatBehavior chatBehavior);
}
