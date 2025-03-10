package com.sprint.mission.discodeit.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContentDto {

    private UUID id;
    private String fileName;
    private String contentType;
    private Long size;
    private byte[] bytes;

}
