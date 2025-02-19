package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.util.type.BinaryContentType;

public record CreateBinaryContentRequest(byte[] content, BinaryContentType contentType) {
}
