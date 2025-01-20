package com.sprint.mission.discodeit.service.message.channelMessage;

import com.sprint.mission.discodeit.entity.message.dto.ChannelMessageInfoResponse;
import com.sprint.mission.discodeit.entity.message.dto.SendChannelMessageRequest;

public interface ChannelMessageService {

    ChannelMessageInfoResponse sendMessage(SendChannelMessageRequest sendChannelMessageRequest);
}
