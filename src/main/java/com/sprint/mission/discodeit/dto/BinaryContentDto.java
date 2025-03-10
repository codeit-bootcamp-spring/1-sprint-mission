package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContentDto {

    private UUID id;
    private String fileName;
    private String contentType;
    private Long size;
    private byte[] bytes;

}
