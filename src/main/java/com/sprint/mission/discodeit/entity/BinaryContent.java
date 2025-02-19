package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private UUID userId;
    private UUID messageId;
    private String fileName;
    private String fileType;
    private String filePath;


    public BinaryContent(BinaryContentDTO dto) throws IOException {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = dto.useerId();
        this.messageId = dto.messageId();
        this.fileName = dto.multipartFile().getOriginalFilename();
        this.fileType = dto.multipartFile().getContentType();

        String uploadFile = "uploadedFile/";
        String originalFile = dto.multipartFile().getOriginalFilename();
        String fileExtension;
        if(originalFile == null || originalFile.lastIndexOf(".") == -1) {
            fileExtension = "";
        } else {
            fileExtension= originalFile.substring(originalFile.lastIndexOf("."));
            Path path = Paths.get(uploadFile + this.getId() + fileExtension);
            File newFile = new File(String.valueOf(path));

            try (FileOutputStream fos  = new FileOutputStream(newFile)){
                fos.write(dto.multipartFile().getBytes());
            }
            this.filePath = newFile.getPath();
        }
    }

}
