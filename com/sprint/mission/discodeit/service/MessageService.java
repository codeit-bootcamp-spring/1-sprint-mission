package discodeit.service;

import discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    Message create();
    Message read(UUID key);
    Message update(UUID key, Message messageToUpdate);
    Message delete(UUID key);
}
