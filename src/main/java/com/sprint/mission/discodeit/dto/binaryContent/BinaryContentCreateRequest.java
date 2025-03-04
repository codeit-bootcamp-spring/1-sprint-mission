package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentCreateRequest(
        String fileNm, String contentType, byte[] content)
{
}
