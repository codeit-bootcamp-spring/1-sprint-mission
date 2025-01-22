package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, List<Message>> channelMessages;
    private final ChannelService channelService;
    private final UserService userService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        this.channelMessages = new HashMap<>();
        this.channelService = channelService;
        this.userService = userService;
    }

    @Override
    public Message createMessage(UUID channelId, UUID authorId, String content) throws IllegalArgumentException {
        if (channelService.getChannelById(channelId) == null) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }

        if (userService.getUserById(authorId) == null) {
            throw new IllegalArgumentException("User not found with ID: " + authorId);
        }

        Message message = new Message(channelId, authorId, content);

        if (!channelMessages.containsKey(channelId)) {
            channelMessages.put(channelId, new ArrayList<>());
        }
        channelMessages.get(channelId).add(message);
        return message;
    }

    @Override
    public Message getMessageById(UUID channelId, UUID messageId) throws IllegalArgumentException {
        if (!channelMessages.containsKey(channelId)) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }

        for (Message message : channelMessages.get(channelId)) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }

        throw new IllegalArgumentException("Message not found with ID: " + messageId + " in channel: " + channelId);
    }
    @Override
    public List<Message> getChannelMessages(UUID channelId) {
        if (!channelMessages.containsKey(channelId)) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }
        return new ArrayList<>(channelMessages.get(channelId));
    }

    @Override
    public void updateMessage(UUID channelId, UUID messageId, String newContent) {
        Message message = getMessageById(channelId, messageId);
        message.updateContent(newContent);
    }

    @Override
    public void deleteMessage(UUID channelId, UUID messageId) throws IllegalArgumentException {
        if (!channelMessages.containsKey(channelId)) {
            throw new IllegalArgumentException("Channel not found with ID: " + channelId);
        }

        List<Message> messages = channelMessages.get(channelId);

        Message messageToDelete = null;
        for (Message message : messages) {
            if (message.getId().equals(messageId)) {
                messageToDelete = message;
                break;
            }
        }

        if (messageToDelete == null) {
            throw new IllegalArgumentException("Message not found with ID: " + messageId + " in channel: " + channelId);
        }

        messages.remove(messageToDelete);

        if (messages.isEmpty()) {
            channelMessages.remove(channelId);
        }
    }
}
