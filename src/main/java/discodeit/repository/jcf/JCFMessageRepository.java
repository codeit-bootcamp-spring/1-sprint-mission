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
        return null;
    }

    @Override
    public Message find(UUID messageId) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        return List.of();
    }

    @Override
    public void update(UUID messageId, String content) {

    }

    @Override
    public void delete(UUID messageId) {

    }
}
