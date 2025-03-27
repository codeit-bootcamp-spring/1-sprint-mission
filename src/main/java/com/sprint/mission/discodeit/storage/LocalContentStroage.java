package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalContentStroage implements BinaryContentStorage {

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
}
