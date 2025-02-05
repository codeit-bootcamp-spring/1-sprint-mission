package com.srint.mission.discodeit.service.basic;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.MessageRepository;
import com.srint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public UUID create(String content, UUID authorId, UUID channelId) {
        if(!Message.validation(content)){
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        Message message = new Message(content, authorId, channelId);
        return messageRepository.save(message);
    }

    @Override
    public Message read(UUID id) {
        Message findMessage = messageRepository.findOne(id);
        return Optional.ofNullable(findMessage)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지가 없습니다."));
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateMessage(UUID id, String message, User user) {
        Message findMessage = messageRepository.findOne(id);
        if(!user.userCompare(findMessage.getAuthorId())){
            throw new IllegalStateException("message 변경 권한이 없습니다.");
        }
        findMessage.setContent(message);
        messageRepository.update(findMessage);
        return findMessage;
    }

    @Override
    public UUID deleteMessage(UUID id, User user) {
        Message findMessage = messageRepository.findOne(id);
        if(!user.userCompare(findMessage.getAuthorId())){
            throw new IllegalStateException("message 삭제 권한이 없습니다.");
        }
        return messageRepository.delete(findMessage.getId());
    }
}
