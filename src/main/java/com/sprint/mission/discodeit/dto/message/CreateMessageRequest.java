package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.User;

import java.util.List;
import java.util.UUID;

public record CreateMessageRequest(List<UUID> attachmentsID, String content, UUID channelID, UUID writerID) {
}
