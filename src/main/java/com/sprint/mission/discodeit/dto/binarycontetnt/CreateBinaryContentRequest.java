package com.sprint.mission.discodeit.dto.binarycontetnt;

public record CreateBinaryContentRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
}