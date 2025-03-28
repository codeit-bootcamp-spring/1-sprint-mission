package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  // 파일 저장
  UUID put(UUID binaryContentId, byte[] bytes);

  // 파일 읽기
  InputStream get(UUID binaryContentId);

  // 파일 다운로드
  ResponseEntity<?> download(BinaryContentDto metaData);
}
