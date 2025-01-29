package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_message implements MessageService {

    private final List<Message> messageList;

    public JCF_message() {
        messageList = new ArrayList<>();
    }

    @Override
    public UUID creat(UUID userId, String content, UUID channelId) {
        Message message = new Message(userId, content, channelId);
        messageList.add(message);
        return message.getId();
    }

    @Override
    public void delete(UUID messageId) {
        Optional<Message> getMessage = messageList.stream().filter(message -> message.getId().equals(messageId)).findFirst();
        if (getMessage.isEmpty()) {
            throw new IllegalArgumentException("Message not found");
        }
        else {
            messageList.remove(getMessage.get());
        }
    }

    @Override
    public void update(UUID messageId, String updateMessage) {
        messageList.stream().filter(message -> message.getId().equals(messageId))
            .forEach(messageContent -> messageContent.updateMessage(updateMessage)
            );
    }

    @Override
    public void DeleteMessageList(List<UUID> deleteMessageList) {
        messageList.removeIf(message -> deleteMessageList.contains(message.getId()));
    }

    //여기아님
    @Override
    public List<String> getMessageList(List<UUID> messageIdList, JCF_user jcfUser) {
        if (messageIdList.isEmpty()) {
            System.out.println("There is no chat in the channel");
            return null;
        }
        else {
            return messageList.stream()
                .filter(message -> messageIdList.stream()
                    .anyMatch(messageId -> message.getId().equals(messageId)))
                .map(message -> "name" +jcfUser.getName(message.getUserId()) + "  " + message.getContent())
                .collect(Collectors.toList());
        }
    }
}
