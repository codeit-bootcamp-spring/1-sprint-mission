package com.sprint.mission.discodeit.db.common;

import com.sprint.mission.discodeit.entity.common.AbstractUUIDEntity;
import java.util.UUID;

public abstract class InMemoryCrudRepository<T extends AbstractUUIDEntity, ID extends UUID>
        implements CrudRepository<T, ID> {
}
