package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileMessageService implements MessageService {

    private final MessageValidator validationService;
    private final MessageRepository fileMessageRepository;

    public FileMessageService(MessageValidator messageValidator,MessageRepository fileMessageRepository) {
        this.validationService = messageValidator;
        this.fileMessageRepository = fileMessageRepository;
    }

    @Override
    public Message createMsg(User user, Channel channel, String content) {
        if (validationService.validateMessage(user, channel, content)){
            Message msg = new Message(user, channel, content);
            fileMessageRepository.save(msg);
            return msg;
        }
        throw new CustomException(ExceptionText.MESSAGE_CREATION_FAILED);
    }

    @Override
    public Message getMessage(UUID msgUuid) {
       return fileMessageRepository.findById(msgUuid);
    }

    @Override
    public Map<UUID, Map<UUID, Message>> getAllMsg() {
        return fileMessageRepository.findAll();
    }

    @Override
    public void updateMsg(UUID msgId, String newContent) {
        Message msg = fileMessageRepository.findById(msgId);
        msg.update(newContent);
        System.out.println("Message content has been updated --> ("+ newContent + ")");
    }

    @Override
    public void deleteMsg(UUID msgUuid) {
        fileMessageRepository.delete(msgUuid);
    }

    @Override
    public void deleteAllMessagesForChannel(UUID channelUuid) {
       fileMessageRepository.deleteAllMessagesForChannel(channelUuid);
    }


}