package com.spirnt.mission.discodeit.dto.message;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class MessageCreateRequest {
    private String content;
    private UUID userId;
    private UUID channelId;
    private List<MultipartFile> files;

    public MessageCreateRequest(UUID userId, UUID channelId,
                                String content, List<MultipartFile> files) {
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
        this.files = (files == null) ? new ArrayList<>() : files;
    }
}
