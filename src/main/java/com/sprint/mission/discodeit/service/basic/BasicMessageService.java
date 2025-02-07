package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository= messageRepository;
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        Message message = new Message(userId, channelId, content);
        return  messageRepository.save(message);
    }

    @Override
    public Message readMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> readAllMessage() {
        List<Message> messageList= new ArrayList<>( messageRepository.load().values());
        return messageList;
    }

    @Override
    public Message modifyMessage(UUID msgID, String content) {
        Message message = messageRepository.findById(msgID);
        message.updateContent(content);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID msgID) {
        messageRepository.delete(msgID);
    }
}
