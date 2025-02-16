package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/*
UUID id;
String fileName;
String filePath;
Long fileSize;
String mimeType;
BinaryContentType type;
Instant createAt;
*/

@Getter
@AllArgsConstructor
public class FindBinaryContentResponse{
    private UUID id;
    private String fileName;
    private String filePath;
    private String mimeType;
    private BinaryContentType type;
    private Instant createdAt;

    private Long fileSize;
}
