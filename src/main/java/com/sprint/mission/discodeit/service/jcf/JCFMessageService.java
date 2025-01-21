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
    private final MessageRepository messageRepository;
    private final MessageValidator validationService;

    public JCFMessageService(MessageValidator validationService, MessageRepository messageRepository) {
        this.validationService = validationService;
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMsg(User user, Channel channel, String content) {
        if (validationService.validateMessage(user, channel, content)){
            Message msg = new Message(user, channel, content);
            messageRepository.save(msg);
            return msg;
        }
        throw new CustomException(ExceptionText.MESSAGE_CREATION_FAILED);
    }

    @Override
    public Message getMessage(UUID msgUuid) {
        return messageRepository.findById(msgUuid);
    }

    @Override
    public Map<UUID, Map<UUID, Message>> getAllMsg() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMsg(UUID msgId, String newContent) {
        Message msg = messageRepository.findById(msgId);
        msg.update(newContent);
        System.out.println("Message content has been updated --> ("+ newContent + ")");
    }

    @Override
    public void deleteMsg(UUID msgUuid) {
        messageRepository.delete(msgUuid);
    }

    @Override
    public void deleteAllMessagesForChannel(UUID channelUuid) {
        messageRepository.deleteAllMessagesForChannel(channelUuid);
    }
}