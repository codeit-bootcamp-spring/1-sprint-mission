package com.sprint.mission.discodeit.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContentStoreDto {
    UUID id;
    byte[] bytes;
    String fileName;
    int size;
    String contentType;
}
