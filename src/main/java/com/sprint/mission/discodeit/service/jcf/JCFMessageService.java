package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.util.*;

public class JCFMessageService implements MessageService {
    //< 채널 UUID < 메시지 UUID, 매시지 객체 >>
    private final Map<UUID, Map<UUID, Message>> msgData = new HashMap<>();
    private final MessageValidator validationService;

    public JCFMessageService(MessageValidator validationService) {
        this.validationService = validationService;
    }

    @Override
    public Message CreateMsg(User user, Channel channel, String content) {
        if (!validationService.validateMessage(user, channel, content)){
            throw new CustomException(ExceptionText.MESSAGE_CREATION_FAILED);
        }
        Message msg = new Message(user, channel, content);
        msgData.putIfAbsent(msg.getDestinationChannel().getuuId(), new HashMap<>());
        msgData.get(msg.getDestinationChannel().getuuId()).put(msg.getMsguuId(), msg);
        return msg;
    }

    @Override
    public Message getMessage(UUID msgId) {
        for (Map<UUID, Message> channelMessages : msgData.values()) {
            if (channelMessages.containsKey(msgId)) {
                return channelMessages.get(msgId);
            }
        }
        System.out.println("Message with ID " + msgId + " not found.");
        return null;
    }

    @Override
    public Map<UUID, Map<UUID, Message>> getAllMsg() {
        return new HashMap<>(msgData);
    }

    @Override
    public void updateMsg(UUID msgId, String newContent) {
        Message msg = getMessage(msgId);
        msg.update(newContent);
        System.out.println("Message content has been updated --> ("+ newContent + ")");

    }

    @Override
    public void deleteMsg(UUID msgId) {
        for (Map<UUID, Message> channelMessages : msgData.values()) {
            if (channelMessages.containsKey(msgId)) {
                channelMessages.remove(msgId);
                System.out.println("Message " + msgId + " deleted.");
                return;
            }
        }
        System.out.println("Message not found: " + msgId);
    }

    @Override
    public void deleteMessagesByChannel(UUID channelId) {
        if (!msgData.containsKey(channelId)) {
            System.out.println("No messages found for channel ID");
        }
        msgData.remove(channelId);
        System.out.println("All messages for channel " + channelId + " have been deleted.");
    }
}
