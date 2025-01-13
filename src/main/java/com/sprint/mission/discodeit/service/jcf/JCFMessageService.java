package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.validation.ValidationService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Map<UUID, Message>> msgData = new HashMap<>();
    private final ValidationService validationService;

    public JCFMessageService(ValidationService validationService) {
        this.validationService = validationService;
    }

    public void addChannelMsg(Message msg) {
        if (!validationService.validate(msg.getDestinationChannel().getuuId(), msg.SendUser().getuuID())) {
            throw new IllegalArgumentException("Invalid channel or user.");
        }
        msgData.putIfAbsent(msg.getDestinationChannel().getuuId(), new HashMap<>());
        msgData.get(msg.getDestinationChannel().getuuId()).put(msg.getMsguuId(), msg);
        System.out.println("Message added successfully: " + msg.getContent());
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
