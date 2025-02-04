package com.sprint.mission.discodeit.service.message.converter;

import com.sprint.mission.discodeit.entity.message.ChannelMessage;
import com.sprint.mission.discodeit.entity.message.dto.ChannelMessageInfoResponse;
import com.sprint.mission.discodeit.entity.message.dto.ChannelMessageInfoResponse.Builder;
import org.springframework.stereotype.Component;

@Component
public class ChannelMessageConverter {

    public ChannelMessageInfoResponse toDto(ChannelMessage message) {
        var response = new ChannelMessageInfoResponse.Builder()
                .messageId(message.getId())
                .sendUserId(message.getMessageSender().getId())
                .receiveChannelId(message.getReceiverChannel().getId())
                .message(message.getMessage())
                .build();

        return response;
    }
}
