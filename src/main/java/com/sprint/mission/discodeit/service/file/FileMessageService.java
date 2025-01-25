package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final MessageRepository repository;
    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(MessageRepository repository, UserService userService, ChannelService channelService) {
        this.repository = repository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        User user = userService.getUser(userId);
        Channel channel = channelService.getChannel(channelId);

        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다. userId: " + userId);
        }
        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다. channelId: " + channelId);
        }

        Message message = new Message(userId, channelId, content);
        repository.save(message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return repository.findAll();
    }

    @Override
    public Message updateMessage(UUID id, String content) {
        Message message = repository.findById(id);
        if (message != null) {
            message.update(content);
            repository.save(message);
            return message;
        }
        return null;
    }

    @Override
    public void deleteMessage(UUID id) {
        repository.delete(id);
    }
}
