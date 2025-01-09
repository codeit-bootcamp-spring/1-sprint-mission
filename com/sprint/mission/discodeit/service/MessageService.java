package sprint.mission.discodeit.service;

import sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    Message create(Message messageToCreate);
    Message read(UUID key);
    Message update(UUID key, Message messageToUpdate);
    Message delete(UUID key);
}
