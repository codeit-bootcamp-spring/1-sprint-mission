package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  /**
   * UUID 를 기반으로 binary data 저장
   *
   * @param id   : binary content id
   * @param data
   * @return : 데이터 저장시 사용된 UUID
   */
  UUID put(UUID id, byte[] data);

  /**
   * UUID 로 binary data 검색
   *
   * @param id
   * @return 데이터 저장시 사용된 UUID
   */
  InputStream get(UUID id);

  /**
   * binary data 다운로드 응답
   *
   * @param binaryContentDto
   */
  ResponseEntity<Resource> download(BinaryContentDto binaryContentDto);

}
