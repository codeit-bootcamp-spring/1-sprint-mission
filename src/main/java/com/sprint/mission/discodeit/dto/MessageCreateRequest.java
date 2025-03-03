package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequest {
    // 변경: senderId → authorId
    private UUID authorId;
    private UUID channelId;
    private String content;
    private UUID attachmentId; // 첨부파일은 선택 사항

    public MessageCreateRequest(UUID authorId, UUID channelId, String content) {
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
        this.attachmentId = null;
    }
}
