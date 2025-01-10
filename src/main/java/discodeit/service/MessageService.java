package discodeit.service;

import discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message);
    Message read(UUID id);
    List<Message> readAll();
    void update(UUID id, String content);
    void delete(UUID id);
}
