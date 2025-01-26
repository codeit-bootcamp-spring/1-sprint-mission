package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(
            MessageRepository messageRepository,
            UserService userService,
            ChannelService channelService
    ) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, String channelId, String userId) {
        if (userService.findById(userId) == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자 ID: " + userId);
        }
        if (channelService.findById(channelId) == null) {
            throw new IllegalArgumentException("존재하지 않는 채널 ID: " + channelId);
        }
        String messageId = UUID.randomUUID().toString();
        Message message = new Message(messageId, content, channelId, userId);
        return messageRepository.save(message);
    }

    @Override
    public Message findById(String messageId) {
        return messageRepository.findById(messageId);
    }
}
