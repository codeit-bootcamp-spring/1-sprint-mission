package com.sprint.mission.discodeit.dto.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class MessageDTO extends MessageBaseDTO{
    private UUID userId;
    private UUID channelId;
    private List<MultipartFile> files;

    public MessageDTO(UUID userId, UUID channelId, String content, List<MultipartFile> files) {
        super(content);
        this.userId = userId;
        this.channelId = channelId;
        if(files != null) {
            this.files = files;
        } else {
            this.files = new ArrayList<>();
        }
    }
}
