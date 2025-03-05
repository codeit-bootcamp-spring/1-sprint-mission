package com.sprint.mission.discodeit.dto.request.message.create;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequest;

import java.util.List;

public record MessageCreateRequestWithFiles(
        MessageCreateRequest messageCreateRequest,
        List<BinaryContentCreateRequest> attachmentRequests
) {
}
