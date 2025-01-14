package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionCode;
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

    public Message CreateMsg(User user, Channel channel, String content) {
        if (!validationService.validateMessage(user, channel, content)){
            throw new CustomException(ExceptionCode.MESSAGE_CREATION_FAILED);
        }
        Message msg = new Message(user, channel, content);
        msgData.putIfAbsent(msg.getDestinationChannel().getuuId(), new HashMap<>());
        msgData.get(msg.getDestinationChannel().getuuId()).put(msg.getMsguuId(), msg);
        return msg;
    }

    public Message getMessage(UUID msgId) {
        for (Map<UUID, Message> channelMessages : msgData.values()) {
            if (channelMessages.containsKey(msgId)) {
                return channelMessages.get(msgId);
            }
        }
        System.out.println("Message with ID " + msgId + " not found.");
        return null;
    }


    public Map<UUID, Map<UUID, Message>> getAllMsg() {
        return new HashMap<>(msgData);
    }


    public void updateMsg(UUID msgId, String newContent) {
        Message msg = getMessage(msgId);
        if (msg != null) {
            msg.update(newContent);
            System.out.println("Message content has been updated --> ("+ newContent + ")");
        } else {
            System.out.println("Message ID " + msgId + " not found.");
        }

        }

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
}
