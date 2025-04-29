package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public class S3BinaryContentStorage implements BinaryContentStorage {

  @Override
  public UUID put(UUID uuid, byte[] bytes) {
    return null;
  }

  @Override
  public InputStream get(UUID uuid) {
    return null;
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    return null;
  }

  @Override
  public void delete(UUID id) {

  }
}
