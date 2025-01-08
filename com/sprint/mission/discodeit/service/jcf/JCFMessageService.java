package discodeit.service.jcf;

import discodeit.entity.Message;
import discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data; // assume that it is repository

    private JCFMessageService() {
        data = new HashMap<>();
    }

    private static final class InstanceHolder {
        private final static JCFMessageService INSTANCE = new JCFMessageService();
    }

    public static JCFMessageService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Message create() {
        Message newMessage = Message.createMessage();
        UUID userId = newMessage.getCommon().getId();
        return data.putIfAbsent(userId, newMessage);
    }

    @Override
    public Message read(UUID key) {
        return data.compute(key, (id, user) -> {
            if (user == null) return Message.createEmptyMessage();

            return user;
        });
    }

    @Override
    public Message update(UUID key, Message messageToUpdate) {
        if (data.containsKey(key))
        {
            UUID newKey = messageToUpdate.getCommon().getId();
            data.put(newKey, messageToUpdate);
            data.remove(key);
            return data.get(newKey);
        }

        return Message.createEmptyMessage();
    }

    @Override
    public Message delete(UUID key) {
        if (data.containsKey(key))
            return data.remove(key);

        return Message.createEmptyMessage();
    }
}
