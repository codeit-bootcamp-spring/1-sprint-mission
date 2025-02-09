package com.sprint.mission.discodeit.dto.binaryContent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentCreateDTO {

    private UUID userId;
    private UUID messageId;
    private byte[] data;
    private String contentType;
    private Long size;
}
