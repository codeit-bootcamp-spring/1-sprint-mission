package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.MessageValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicMassageService implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageValidator messageValidator;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMassageService(
            @Qualifier("fileMessageRepository") MessageRepository messageRepository,
            ChannelService channelService,
            UserService userService
    ) {
        this.messageValidator = new MessageValidator();
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(String content, UUID channelId, UUID writerId) {
        Channel channel = channelService.searchById(channelId);
        User user = userService.searchById(writerId);

        if (messageValidator.inValidContent(content)) {
            Message newMessage = Message.createMessage(channel, user, content);
            messageRepository.save(newMessage);
            System.out.println("create message: " + content);
            return newMessage;
        }
        return null;
    }

    @Override
    public List<Message> getAllMessageList() {
        return messageRepository.findAll();
    }

    @Override
    public Message searchById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("message does not exist"));
    }

    @Override
    public void updateMessage(UUID id, String content) {
        Message message = searchById(id);
        if (messageValidator.inValidContent(content)) {
            message.update(content);
            messageRepository.save(message);
            System.out.println("success updateChannel");
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }
}
