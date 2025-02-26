package com.sprint.mission.discodeit.entity.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class MessageSendFileRequest{
    private String fileName;
    private MultipartFile file;
    private List<MultipartFile> files;
}
