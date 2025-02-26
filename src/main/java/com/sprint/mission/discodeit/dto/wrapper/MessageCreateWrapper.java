package com.sprint.mission.discodeit.dto.wrapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;

import java.util.List;

public record MessageCreateWrapper(
        MessageCreateRequest messageCreateRequest,
        List<BinaryContentRequest> binaryContentRequests
) {
}
