package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.BinaryContentStoreDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@NoArgsConstructor
public class BinaryContentMapper {
    public BinaryContentDto toDto(BinaryContent binaryContent) {

        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()
        );
    }

    // static이면 컨트롤러 계층에 mapper 의존성 주입 안하고도 BinaryContentMapper.toDto~ 이렇게 바로 사용 가능
    public static List<BinaryContentStoreDto> toDtoFromMultipartFile(List<MultipartFile> attachments) {
        List<BinaryContentStoreDto> attachmentRequests = Optional.ofNullable(attachments)
                .map(files -> files.stream()
                        .map(file -> {
                            try {
                                return new BinaryContentStoreDto(
                                        UUID.randomUUID(),
                                        file.getBytes(),
                                        file.getOriginalFilename(),
                                        (int) file.getSize(),
                                        file.getContentType()
                                );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList())
                .orElse(new ArrayList<>());
        return attachmentRequests;
    }
}