package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID uuid, byte[] data) throws IOException;

  InputStream get(UUID uuid) throws IOException;

  ResponseEntity<?> download(BinaryContentResponse response);
}
