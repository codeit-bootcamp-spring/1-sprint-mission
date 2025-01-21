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
    public Message createMsg(User user, Channel channel, String content) {
        if (validationService.validateMessage(user, channel, content)){
            Message msg = new Message(user, channel, content);
            data.putIfAbsent(msg.getDestinationChannel().getuuId(), new HashMap<>());
            data.get(msg.getDestinationChannel().getuuId()).put(msg.getMsguuId(), msg);
            return msg;
        }
        throw new CustomException(ExceptionText.MESSAGE_CREATION_FAILED);
    }

    @Override
    public Message getMessage(UUID msgUuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(msgUuid)) {
                return channelMessages.get(msgUuid);
            }
        }
        System.out.println("Message with ID " + msgUuid + " not found.");
        return null;
    }

    @Override
    public Map<UUID, Map<UUID, Message>> getAllMsg() {
        return  new HashMap<>(data);
    }

    @Override
    public void updateMsg(UUID msgUuid, String newContent) {
        Message msg = getMessage(msgUuid);
        msg.update(newContent);
        System.out.println("Message content has been updated --> ("+ newContent + ")");
    }

    @Override
    public void deleteMsg(UUID msgUuid) {
        for (Map<UUID, Message> channelMessages : data.values()) {
            if (channelMessages.containsKey(msgUuid)) {
                channelMessages.remove(msgUuid);
                System.out.println("Message " + msgUuid + " deleted.");
            }
        }
        System.out.println("Message not found: " + msgUuid);
    }

    @Override
    public void deleteAllMessagesForChannel(UUID channelUuid) {
        if (!data.containsKey(channelUuid)) {
            System.out.println("No messages found for channel ID");
        }
        data.remove(channelUuid);
        System.out.println("All messages for channel "+ "'" + channelUuid +"'" +" have been deleted.");
    }
}