package com.sprint.mission.repository.binary;

import com.sprint.mission.dto.response.BinaryContentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.UUID;

@Repository
public interface BinaryContentStorage {

  UUID put(UUID id, byte[] content);

  InputStream get(UUID id);

  ResponseEntity<?> download(BinaryContentDto content);
}
