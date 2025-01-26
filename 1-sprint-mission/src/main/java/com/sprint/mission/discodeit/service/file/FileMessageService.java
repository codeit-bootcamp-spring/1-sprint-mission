package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, UUID userId, UUID channelId) {
        // 사용자와 채널 존재 여부 검증
        if (userService.findById(userId) == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자 ID: " + userId);
        }
        if (channelService.findById(channelId) == null) {
            throw new IllegalArgumentException("존재하지 않는 채널 ID: " + channelId);
        }

        Message message = new Message(content, userId, channelId);
        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, String newContent) {
        Message existing = messageRepository.findById(id);
        if (existing != null) {
            existing.update(newContent);
            return messageRepository.save(existing);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
