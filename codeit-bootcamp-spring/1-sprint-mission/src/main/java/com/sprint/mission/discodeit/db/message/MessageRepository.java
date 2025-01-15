package com.sprint.mission.discodeit.db.message;

import com.sprint.mission.discodeit.db.common.CrudRepository;
import com.sprint.mission.discodeit.entity.message.Message;
import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message, UUID> {
}
