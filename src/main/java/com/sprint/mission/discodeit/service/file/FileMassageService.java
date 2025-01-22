package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class FileMassageService implements MessageService {

    MessageRepository fileMessageRepository;

    // mocking 이용으로 추가
    private InputHandler inputHandler;

    public FileMassageService(MessageRepository fileMessageRepository, InputHandler inputHandler){
        this.fileMessageRepository = fileMessageRepository;
        this.inputHandler = inputHandler;
    }


    @Override
    public UUID createMessage(Channel channel, String messageText) {
        Message message = new Message(channel, messageText);
        fileMessageRepository.saveMessage(message);
        return message.getId();
    }

    @Override
    public int showAllMessages() {
        return fileMessageRepository.findAllMessages().size();
    }

    @Override
    public Message getMessageById(UUID id) {
        return fileMessageRepository.findMessageById(id);
    }

    @Override
    public void updateMessageText(UUID id) {
        String newMessageText = inputHandler.getNewInput();

        Message message = getMessageById(id);
        message.setMessageText(newMessageText);

        fileMessageRepository.saveMessage(message);
    }

    @Override
    public void deleteAllMessages() {
        fileMessageRepository.deleteAllMessages();
    }

    @Override
    public void deleteMessageById(UUID id) {
        fileMessageRepository.deleteMessageById(id);
    }
}
