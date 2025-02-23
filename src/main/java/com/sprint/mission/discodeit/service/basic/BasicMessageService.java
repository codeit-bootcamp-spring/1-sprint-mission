package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    @Autowired
    private final MessageRepository messageRepository;
    @Autowired
    private final UserService userService;

    private final ChannelService channelService;

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        if (userService.getUser(userId) == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다. userId: " + userId);
        }
        if (channelService.getChannel(channelId) == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다. channelId: " + channelId);
        }

        Message message = new Message(userId, channelId, content);
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateMessage(UUID id, String content) {
        Message message = messageRepository.findById(id);
        if (message != null) {
            message.update(content);
            messageRepository.save(message);
            return message;
        }
        return null;
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.delete(id);
    }
}
