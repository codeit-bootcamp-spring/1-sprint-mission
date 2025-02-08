package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.util.*;

public class JCFMessageService implements MessageService {
    //< 채널 UUID < 메시지 UUID, 매시지 객체 >>
    private final Map<UUID, Map<UUID, Message>> data = new HashMap<>();
    private final MessageValidator validationService;

    public JCFMessageService(MessageValidator validationService) {
        this.validationService = validationService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        if(validationService.validateMessage(authorId, channelId, content)){
            Message msg = new Message(content, channelId, authorId);
            data.putIfAbsent(channelId, new HashMap<>());
            data.get(channelId).put(msg.getId(), msg);
            return msg;
        }
        throw new CustomException(ExceptionText.MESSAGE_CREATION_FAILED);
    }

    @Override
    public Message find(UUID messageId) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(messageId)) {
                return channelMessages.get(messageId);
            }
        }
        System.out.println("Message with ID " + messageId + " not found.");
        return null;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        for (Map<UUID, Message> channelMessages : data.values()) {
            messages.addAll(channelMessages.values()); // 각 채널의 메시지들을 추가
        }
        return messages;
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message msg = find(messageId);
        msg.update(newContent);
        System.out.println("Message content has been updated --> ("+ newContent + ")");
        return msg;
    }

    @Override
    public void delete(UUID messageId) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(messageId)) {
                channelMessages.remove(messageId);
                System.out.println("Message " + messageId + " deleted.");
            }
        }
        System.out.println("Message not found: " + messageId);
    }

}