package discodeit.jcf;

import discodeit.entity.Message;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(ChannelService channelService, UserService userService) {
        data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        try {
            channelService.find(channelId);
            userService.find(authorId);
        } catch(NoSuchElementException e) {
            throw e;
        }

        Message message = new Message(content, channelId, authorId);
        data.put(message.getId(), message);

        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = data.get(messageId);

        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = data.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        message.update(newContent);

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if(!data.containsKey(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        data.remove(messageId);
    }
}
