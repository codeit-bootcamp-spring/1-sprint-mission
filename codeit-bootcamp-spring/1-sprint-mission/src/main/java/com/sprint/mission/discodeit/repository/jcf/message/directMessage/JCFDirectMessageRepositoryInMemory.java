package com.sprint.mission.discodeit.repository.jcf.message.directMessage;

import com.sprint.mission.discodeit.repository.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.message.DirectMessage;
import java.util.UUID;

public class JCFDirectMessageRepositoryInMemory extends InMemoryCrudRepository<DirectMessage, UUID>
        implements DirectMessageRepository {

    private JCFDirectMessageRepositoryInMemory() {}

    public static JCFDirectMessageRepositoryInMemory getInstance() {
        return new JCFDirectMessageRepositoryInMemory();
    }

}
