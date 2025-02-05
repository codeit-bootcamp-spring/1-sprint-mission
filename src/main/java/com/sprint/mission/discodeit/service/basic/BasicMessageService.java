package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.MessageValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageValidator validator;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepository, MessageValidator validator, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.validator = validator;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, User sender, UUID channelId) {
        userService.find(sender.getId());
        channelService.find(channelId);
        validator.validate(content);
        return messageRepository.save(content, sender, channelId);
    }

    @Override
    public Message find(UUID messageId) {
        return Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public String getInfo(UUID messageId) {
        return find(messageId).toString();
    }

    @Override
    public void update(UUID messageId, String content) {
        Message message = find(messageId);
        messageRepository.update(message, content);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = find(messageId);
        messageRepository.delete(message);
    }
}
