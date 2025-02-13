package com.sprint.mission.discodeit.dto.wrapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;

import java.util.List;

public record MessageCreateRequestWrapper(
        MessageCreateRequest messageCreateRequest,
        List<BinaryContentCreateRequest> binaryContentCreateRequestList
 ) {
}
