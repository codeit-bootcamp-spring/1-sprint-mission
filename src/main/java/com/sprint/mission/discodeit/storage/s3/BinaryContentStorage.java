package com.sprint.mission.discodeit.storage.s3;

import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {

    UUID put(UUID id, byte[] content);

    InputStream get(UUID id);

    ResponseEntity<Void> download(BinaryContentDto dto);
}
