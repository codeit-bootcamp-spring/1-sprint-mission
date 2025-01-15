package com.sprint.mission.discodeit.db.message.directMessage;

import com.sprint.mission.discodeit.db.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.message.DirectMessage;
import java.util.UUID;

public class MessageRepositoryInMemory extends InMemoryCrudRepository<DirectMessage, UUID>
        implements DirectMessageRepository {

    private MessageRepositoryInMemory() {}

    public static MessageRepositoryInMemory getInstance() {
        return new MessageRepositoryInMemory();
    }

}
