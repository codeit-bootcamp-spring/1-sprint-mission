package com.sprint.mission.discodeit.service.message.converter;

import com.sprint.mission.discodeit.entity.message.DirectMessage;
import com.sprint.mission.discodeit.entity.message.dto.DirectMessageInfoResponse;
import com.sprint.mission.discodeit.entity.message.dto.DirectMessageInfoResponse.Builder;
import org.springframework.stereotype.Component;

@Component
public class DirectMessageConverter {

    public DirectMessageInfoResponse toDto(DirectMessage directMessage) {
        var response = new Builder()
                .messageId(directMessage.getId())
                .sender(directMessage.getMessageSender().getId())
                .receiver(directMessage.getMessageReceiver().getId())
                .message(directMessage.getMessage())
                .build();

        return response;
    }
}
