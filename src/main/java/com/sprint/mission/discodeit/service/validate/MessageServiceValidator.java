package com.sprint.mission.discodeit.service.validate;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;

import java.util.Optional;

public class MessageServiceValidator implements ServiceValidator<Message> {
    @Override
    public Message entityValidate(Message message) {
        //System.out.println("MessageServiceValidator.entityValidate: " + message.hashCode());

        return Optional.ofNullable(message)
                .orElseThrow(MessageNotFoundException::new);
    }
}
