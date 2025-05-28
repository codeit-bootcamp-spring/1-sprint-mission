package com.sprint.mission.discodeit.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BinaryContentResponse(
    UUID id,
    String fileName,
    Long size,
    String contentType
) {

//  public static BinaryContentResponse entityToDto(BinaryContent binaryContent) {
//    return BinaryContentResponse.builder()
//        .id(binaryContent.getId())
//        .fileName(binaryContent.getFileName())
//        .size(binaryContent.getSize())
//        .contentType(binaryContent.getContentType())
//        .bytes(binaryContent.getBytes())
//        .build();
//  }
}
