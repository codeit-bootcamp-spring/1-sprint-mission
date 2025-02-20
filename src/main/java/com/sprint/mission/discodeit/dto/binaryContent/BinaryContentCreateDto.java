package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentCreateDto(String fileName, String contentType, byte[] bytes) {
}
