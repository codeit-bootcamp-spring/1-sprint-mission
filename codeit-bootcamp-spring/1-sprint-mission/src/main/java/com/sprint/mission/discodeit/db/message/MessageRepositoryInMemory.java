package com.sprint.mission.discodeit.db.message;

import com.sprint.mission.discodeit.db.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.message.Message;
import java.util.UUID;

public class MessageRepositoryInMemory extends InMemoryCrudRepository<Message, UUID> implements MessageRepository {
}
