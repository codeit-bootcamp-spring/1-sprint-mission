package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    //
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message create(String content, UUID channelId, UUID writerId) {
        if (!channelRepository.existsId(channelId)) {
            throw new NoSuchElementException("채널이 존재하지 않습니다.");
        }
        if (!userRepository.existsId(writerId)) {
            throw new NoSuchElementException("유저가 존재하지 않습니다.");
        }

        Message message = new Message(content, channelId, writerId);
        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));
    }

    @Override
    public Message findByChannel(UUID channelId) {
        return null;
    }

    @Override
    public Message findByUser(UUID userId) {
        return null;
    }


    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));
        message.updated(newContent);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsId(messageId)) {
            throw new NoSuchElementException("메시지가 존재하지 않습니다.");
        }
        messageRepository.delete(messageId);
    }
}
