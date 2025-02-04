package com.sprint.mission.discodeit.service.validate;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileMessageServiceValidator extends MessageServiceValidator {
    @Override
    public Map<UUID, Message> entityValidate(Map<UUID, Message> messageMap) {
        return Optional.ofNullable(messageMap)
                .orElseThrow(MessageNotFoundException::new);
    }
}
