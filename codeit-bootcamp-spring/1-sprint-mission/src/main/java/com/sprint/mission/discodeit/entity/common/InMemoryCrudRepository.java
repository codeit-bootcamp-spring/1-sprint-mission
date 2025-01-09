package com.sprint.mission.discodeit.entity.common;

import com.sprint.mission.discodeit.db.common.CrudRepository;
import java.util.UUID;

public abstract class InMemoryCrudRepository<T extends AbstractUUIDEntity, ID extends UUID>
        implements CrudRepository<T, ID> {
}
