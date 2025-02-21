package com.sprint.mission.discodeit.dto.request;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

    public final static BinaryContentCreateRequest EMPTY_REQUEST =
        new BinaryContentCreateRequest("", "", new byte[0]);

    public boolean isEmpty() {
        return this == EMPTY_REQUEST ||
            fileName.isEmpty() || contentType.isEmpty() || bytes.length == 0;
    }
}
