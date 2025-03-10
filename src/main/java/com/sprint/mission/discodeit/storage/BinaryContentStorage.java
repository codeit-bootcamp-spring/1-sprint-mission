package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public interface BinaryContentStorage {

    UUID put(UUID id, byte[] data) throws IOException;

    InputStream get(UUID id) throws IOException;

    ResponseEntity<?> download(BinaryContentDto binaryContentDto) throws IOException;
}
