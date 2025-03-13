package com.sprint.mission.discodeit.storage;

import org.springframework.http.ResponseEntity;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
    UUID put(UUID id, byte[] data) throws IOException;
    InputStream get(UUID id) throws IOException;
    ResponseEntity<?> download(BinaryContentDto dto) throws IOException;
}
