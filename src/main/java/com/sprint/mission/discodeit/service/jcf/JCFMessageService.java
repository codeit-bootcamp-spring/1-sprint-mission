package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
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
    public Message createMsg(String content, UUID channelId, UUID authorId){
        if (validationService.validateMessage(authorId, channelId, content)){
            Message msg = new Message(content,channelId,authorId);
            data.putIfAbsent(msg.getChannelId(), new HashMap<>());
            data.get(msg.getChannelId()).put(msg.getId(), msg);
            return msg;
        }
        throw new CustomException(ExceptionText.MESSAGE_CREATION_FAILED);
    }

    @Override
    public Message find(UUID msgUuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(msgUuid)) {
                return channelMessages.get(msgUuid);
            }
        }
        System.out.println("Message with ID " + msgUuid + " not found.");
        return null;
    }

    @Override
    public List<Message> findAll() {
        List<Message> allMessages = new ArrayList<>();

        for (Map<UUID, Message> channelMessages : data.values()) {
            allMessages.addAll(channelMessages.values()); // 각 채널의 모든 메시지를 추가
        }

        return allMessages;
    }

    @Override
    public Message update(UUID msgUuid, String newContent) {
        Message msg = find(msgUuid);
        msg.update(newContent);
        System.out.println("Message content has been updated --> ("+ newContent + ")");
        return null;
    }

    @Override
    public void delete(UUID msgUuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(msgUuid)) {
                channelMessages.remove(msgUuid);
                System.out.println("Message " + msgUuid + " deleted.");
            }
        }
        System.out.println("Message not found: " + msgUuid);
    }
}