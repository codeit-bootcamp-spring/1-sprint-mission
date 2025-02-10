package com.sprint.mission.discodeit.repository.message.interfaces;

import com.sprint.mission.discodeit.domain.message.Message;

public interface MessageRepository {

    Message save(Message message);

}
