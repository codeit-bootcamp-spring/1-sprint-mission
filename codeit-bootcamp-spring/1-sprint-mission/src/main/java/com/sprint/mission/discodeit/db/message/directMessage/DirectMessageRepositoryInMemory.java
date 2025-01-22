package com.sprint.mission.discodeit.db.message.directMessage;

import com.sprint.mission.discodeit.db.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.message.DirectMessage;
import java.util.UUID;

public class DirectMessageRepositoryInMemory extends InMemoryCrudRepository<DirectMessage, UUID>
        implements DirectMessageRepository {

    private DirectMessageRepositoryInMemory() {}

    public static DirectMessageRepositoryInMemory getInstance() {
        return new DirectMessageRepositoryInMemory();
    }

}
