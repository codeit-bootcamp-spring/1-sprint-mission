package discodeit.repository.jcf;

import discodeit.enity.Message;
import discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public void delete(UUID messageId) {
        try {
            if (!data.containsKey(messageId))
                throw new NoSuchElementException("Message ID: " + messageId + " not found");
            data.remove(messageId);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public Message findById(UUID messageId) {
        Message message = this.data.get(messageId);
        return Optional.ofNullable(message)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " not found"));
    }

    public Map<UUID, Message> findAll() {
        return new HashMap<>(data);
    }
}
