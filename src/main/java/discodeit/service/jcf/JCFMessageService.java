package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;
import discodeit.validator.MessageValidator;
import discodeit.validator.Validator;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> messages;
    private final MessageValidator validator;
    private UserService userService;
    private ChannelService channelService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.messages = new HashMap<>();
        this.validator = new MessageValidator();
        this.userService = userService;
        this.channelService = channelService;
    }

    private static class JCFMessageServiceHolder {
        private static MessageService instance;

        private static void initialize(UserService userService, ChannelService channelService) {
            instance = new JCFMessageService(userService, channelService);
        }

        private static MessageService getInstance() {
            if (instance == null) {
                throw new IllegalStateException("[ERROR] JCFMessageService is not initialized");
            }
            return instance;
        }
    }

    public static void initialize(UserService userService, ChannelService channelService) {
        JCFMessageServiceHolder.initialize(userService, channelService);
    }

    public static MessageService getInstance() {
        return JCFMessageServiceHolder.getInstance();
    }

    @Override
    public Message create(String content, User sender, UUID channelId) {
        userService.find(sender.getId());
        channelService.find(channelId);
        validator.validate(content);

        Message message = new Message(content, sender, channelId);
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message foundMessage = messages.get(messageId);

        return Optional.ofNullable(foundMessage)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다."));
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
            throw new NoSuchElementException("[ERROR] 존재하지 않는 메시지입니다.");
        }
        messages.remove(messageId);
    }
}
