package discodeit.jcf;

import discodeit.entity.Message;
import discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        data = new HashMap<>();
    }
    @Override
    public void create(Message message) {
        data.put(message.getId(), message);
        System.out.println("Message created: " + message.getContent());
    }

    @Override
    public Message read(UUID id) {
        if (!data.containsKey(id)) {
            System.out.println("Message not found");
            return null;
        }
        System.out.println("Message read: " + data.get(id).getContent());
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, String content) {
        Message message = data.get(id);
        if (message != null) {
            System.out.print("Message updated: " + message.getContent());
            message.updateContent(content);
            message.updateUpdatedAt();
            System.out.println(" -> " + message.getContent());
        }
    }

    @Override
    public void delete(UUID id) {
        if(!data.containsKey(id)) {
            System.out.println("Message not found for delete");
        }
        data.remove(id);
        System.out.println("Message deleted");
    }
}
