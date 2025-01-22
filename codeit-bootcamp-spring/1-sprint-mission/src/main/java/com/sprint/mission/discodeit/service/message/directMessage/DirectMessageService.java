package com.sprint.mission.discodeit.service.message.directMessage;

import com.sprint.mission.discodeit.entity.message.dto.DirectMessageInfoResponse;
import com.sprint.mission.discodeit.entity.message.dto.SendDirectMessageRequest;

public interface DirectMessageService {

    DirectMessageInfoResponse sendMessage(SendDirectMessageRequest sendDirectMessageRequest);
}
