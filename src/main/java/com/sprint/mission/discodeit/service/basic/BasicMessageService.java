package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.MessageUtil.validContent;
import static com.sprint.mission.discodeit.util.MessageUtil.validMessageId;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        validContent(content);

        Message message = new Message(content, authorId, channelId);
        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID contentId) {
        Message message = messageRepository.findById(contentId);
        validMessageId(message);
        return message;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID contentId, String content) {
        Message checkMessage = messageRepository.findById(contentId);

        validMessageId(checkMessage);
        validContent(content);

        checkMessage.update(content);
        return messageRepository.save(checkMessage);
    }

    @Override
    public void delete(UUID contentId) {
        Message checkMessage = messageRepository.findById(contentId);

        validMessageId(checkMessage);
        messageRepository.delete(contentId);
    }
}
