package com.sprint.mission.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MessageDtoForUpdate {
    private UUID messageId;
    private String content;
    private List<MultipartFile> binaryContentList;

    public List<byte[]> getBinaryContentList() {
        return binaryContentList.stream()
                .map((content) -> {
                    try {
                        return content.getBytes();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

}
