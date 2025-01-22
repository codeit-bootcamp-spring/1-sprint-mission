package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.impl.InMemoryMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public JCFMessageService() {
        this.messageRepository = new InMemoryMessageRepository();
    }

    @Override
    public void createCommonMessage(User sender, String content) {
        Message message = Message.ofCommon(sender, content);
        messageRepository.save(message);
    }

    @Override
    public void createReplyMessage(User sender, String content) {
        Message message = Message.ofReply(sender, content);
        messageRepository.save(message);
    }

    @Override
    public Message findMessageById(UUID id) {
        return messageRepository.findMessageById(id)
            .orElseThrow(() -> new IllegalArgumentException("message not found: " + id));
    }

    @Override
    public List<Message> findMessageBySender(UUID senderId) {
        List<Message> messages = messageRepository.findMessagesBySender(senderId);
        if (messages.isEmpty()) {
            throw new IllegalArgumentException("Messages not found for sender: " + senderId);
        }
        return messages;
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAllMessages();
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        messageRepository.findMessageById(id).ifPresentOrElse(m -> m.updateContent(newContent), () -> {
            throw new IllegalArgumentException("message not found: " + id);
        });
    }

    @Override
    public void removeMessage(UUID id) {
        messageRepository.remove(id);
    }
}
