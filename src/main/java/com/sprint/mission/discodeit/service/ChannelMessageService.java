package com.sprint.mission.discodeit.service;

import java.time.Instant;
import java.util.UUID;

public interface ChannelMessageService {

    void getLastMessageTime(UUID channelId);
}
