package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.response.CreateMessageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public Message toEntity(User sendUser, Channel channel, String content) {
        return Message.of(sendUser, channel, content);
    }

    public CreateMessageResponse toCreateMessageResponse(Message message) {
        return new CreateMessageResponse(message.getId(), message.getContent());
    }
}
