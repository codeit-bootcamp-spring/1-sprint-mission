package discodeit.repository.jcf;

import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messages;

    public JCFMessageRepository() {
        this.messages = new HashMap<>();
    }

    @Override
    public Message save(String content, User sender, UUID channelId) {
        Message message = new Message(content, sender, channelId);
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return messages.values().stream().toList();
    }

    @Override
    public void update(UUID messageId, String content) {
        messages.get(messageId).updateContent(content);
    }

    @Override
    public void delete(UUID messageId) {
        messages.remove(messageId);
    }
}
