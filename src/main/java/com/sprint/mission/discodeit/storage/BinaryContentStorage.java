package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  // 저장 : UUID 키 정보를 바탕으로 byte[] 데이터를 저장합니다.
  CompletableFuture<Void> put(UUID id, byte[] bytes);
//  UUID put(UUID id, byte[] bytes);

  // 조회 : 키 정보를 바탕으로 byte[] 데이터를 읽어 InputStream 타입으로 반환
  InputStream get(UUID id);

  // 다운로드 : BinaryContentDto 정보를 바탕으로 파일을 다운로드할 수 있는 응답을 반환
  ResponseEntity<?> download(BinaryContentDto binaryContentDto);

}
