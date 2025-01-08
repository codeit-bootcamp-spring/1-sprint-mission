package com.sprint.mission.discodeit.service.common;

import java.io.Serializable;
import java.util.UUID;

public interface InMemoryRepository<T, ID extends Serializable> extends Repository<T, ID>{
}
