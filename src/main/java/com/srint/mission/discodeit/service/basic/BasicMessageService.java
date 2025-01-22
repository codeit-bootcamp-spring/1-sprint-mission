package com.srint.mission.discodeit.service.basic;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.MessageRepository;
import com.srint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public UUID create(String content, User user, Channel channel) {
        Message message = new Message(content, user, channel);
        message.setMessageChannel();
        return messageRepository.save(message);
    }

    @Override
    public Message read(UUID id) {
        return messageRepository.findOne(id);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, String message, User user) {
        Message findMessage = messageRepository.findOne(id);
        if(!findMessage.getUser().userCompare(user)){
            throw new IllegalStateException("message 변경 권한이 없습니다.");
        }
        findMessage.setContent(message);
        messageRepository.save(findMessage);
        return findMessage;
    }

    @Override
    public UUID deleteMessage(UUID id, User user) {
        Message findMessage = messageRepository.findOne(id);
        if(!findMessage.getUser().userCompare(user)){
            throw new IllegalStateException("message 삭제 권한이 없습니다.");
        }
        findMessage.getChannel().deleteMessage(findMessage);
        return messageRepository.delete(findMessage.getId());
    }
}
