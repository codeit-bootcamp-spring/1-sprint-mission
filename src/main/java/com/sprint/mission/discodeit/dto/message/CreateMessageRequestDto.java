package com.sprint.mission.discodeit.dto.message;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateMessageRequestDto {
    private UUID messageId;
    private String content;
    private UUID channelId;
    private UUID authorId;
    private List<MultipartFile> attachments;

    public CreateMessageRequestDto(String content, UUID channelId, UUID authorId) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }
}
