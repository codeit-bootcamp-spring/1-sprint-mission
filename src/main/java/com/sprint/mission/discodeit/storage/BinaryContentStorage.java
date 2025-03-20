package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID fileId, byte[] bytes);

  InputStream get(UUID fileId);

  ResponseEntity<?> download(BinaryContentDto file);

  Path resolvePath(UUID id);

}
