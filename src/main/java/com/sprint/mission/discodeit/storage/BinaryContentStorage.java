package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
    UUID put(UUID fileId, MultipartFile file);

    InputStream get(UUID fileId);

    ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponseDto);
}
