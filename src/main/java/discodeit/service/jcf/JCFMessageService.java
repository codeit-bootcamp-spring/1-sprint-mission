package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> messages;
    private UserService userService;
    private ChannelService channelService;

    private JCFMessageService() {
        messages = new HashMap<>();
    }

    private static class JCFMessageServiceHolder {
        private static final MessageService INSTANCE = new JCFMessageService();
    }

    public static MessageService getInstance() {
        return JCFMessageServiceHolder.INSTANCE;
    }

    @Override
    public void updateUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void updateChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, User sender, UUID channelId) {
        userService.find(sender.getId());
        channelService.find(channelId);

        Message message = new Message(content, sender, channelId);
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message foundMessage = messages.get(messageId);

        return Optional.ofNullable(foundMessage)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 메시지입니다."));
    }

    @Override
    public List<Message> findAll() {
        return messages.values().stream().toList();
    }

    @Override
    public String getInfo(UUID messageId) {
        Message message = find(messageId);
        return message.toString();
    }

    @Override
    public void update(UUID messageId, String content) {
        Message message = find(messageId);
        message.updateContent(content);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messages.containsKey(messageId)) {
            throw new NoSuchElementException("존재하지 않는 메시지입니다.");
        }
        messages.remove(messageId);
    }
}
