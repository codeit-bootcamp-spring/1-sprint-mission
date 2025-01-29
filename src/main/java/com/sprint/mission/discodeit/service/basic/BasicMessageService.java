package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public void createMessage(Message message) {
        messageRepository.createMessage(message);
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        User user = userRepository.getUser(authorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다: " + authorId));
        Channel channel = channelRepository.getChannel(channelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 채널을 찾을 수 없습니다: " + channelId));

        Message message = new Message(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis(), content, user, channel, authorId, channelId);
        messageRepository.createMessage(message);  // Repository에 저장
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return messageRepository.getMessage(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.getAllMessages();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        Message message = getMessage(id);
        message.update(content);
        messageRepository.updateMessage(id, content);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteMessage(id);
    }
}
