package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMessageService(
            MessageRepository messageRepository,
            UserService userService,
            ChannelService channelService
    ) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(Message message) {
        // 채널 / 사용자 유효성 검사
        if (userService.findById(message.getUserId().toString()) == null) {
            throw new IllegalArgumentException("Invalid user ID: " + message.getUserId());
        }
        if (channelService.findById(message.getChannelId().toString()) == null) {
            throw new IllegalArgumentException("Invalid channel ID: " + message.getChannelId());
        }
        if (message.getId() == null) {
            message.setId(UUID.randomUUID());
        }
        return messageRepository.save(message);
    }

    @Override
    public Message findById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return messageRepository.findById(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(String id, Message updated) {
        try {
            UUID uuid = UUID.fromString(id);
            Message existing = messageRepository.findById(uuid);
            if (existing == null) {
                throw new RuntimeException("Message not found for ID: " + id);
            }
            existing.update(updated.getContent());
            messageRepository.save(existing);
            return existing;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            messageRepository.delete(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }
}
