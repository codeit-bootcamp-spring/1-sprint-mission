package com.sprint.mission.discodeit.dto.response;

import java.util.UUID;

public record BinaryContentUploadEvent(UUID binaryContentId, byte[] bytes) {

}
