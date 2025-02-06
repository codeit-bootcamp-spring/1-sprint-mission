package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.Value;

@Value(staticConstructor = "of")
public class MessageDto {
    User writer;
    String content;
    Channel channel;
}
