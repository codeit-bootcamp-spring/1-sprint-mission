package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentResponse {
    private UUID id;
    private UUID userId;
    private UUID messageId;
    private String mimeType;
    private long fileSize;

    public BinaryContentResponse(BinaryContent binaryContent) {
        this.id = binaryContent.getId();
        this.userId = binaryContent.getUser().getId();
        this.messageId = binaryContent.getMessage().getId();
        this.mimeType = binaryContent.getMimeType();
        this.fileSize = binaryContent.getData().length;
    }
}
