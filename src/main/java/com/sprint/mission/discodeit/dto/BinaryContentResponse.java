package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.lang.management.LockInfo;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record BinaryContentResponse(
    UUID id,
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
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
