package discodeit.service.basic;

import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.repository.MessageRepository;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChannelService channelService;

    public BasicMessageService(MessageRepository messageRepository, UserService userService, ChannelService channelService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public void updateUserService(UserService userService) {

    }

    @Override
    public void updateChannelService(ChannelService channelService) {

    }

    @Override
    public Message create(String content, User sender, UUID channelId) {
        userService.find(sender.getId());
        channelService.find(channelId);
        return messageRepository.save(content, sender, channelId);
    }

    @Override
    public Message find(UUID messageId) {
        return Optional.ofNullable(messageRepository.find(messageId))
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 메시지입니다."));
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
