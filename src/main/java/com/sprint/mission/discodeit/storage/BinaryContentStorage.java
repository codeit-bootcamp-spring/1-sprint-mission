package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

// 바이너리 데이터의 저장 및 로드를 담당하는 컴포넌트
public interface BinaryContentStorage {

  // UUID 키 정보를 바탕으로 byte[] 데이터 저장 = ID별로 경로를 나눠서 데이터 저장
  // UUID는 BinaryContent의 Id
  UUID put(UUID id, byte[] bytes);

  // 키 정보를 바탕으로 byte[] 데이터를 읽어 InputStream 타입으로 반환
  // UUID는 BinaryContent의 Id
  InputStream get(UUID id);

  // HTTP API로 다운로드 기능 제공
  // BinaryContentDto 정보를 바탕으로 파일을 다운로드할 수 있는 응답을 반환
  ResponseEntity<Resource> download(BinaryContentDto binaryContentDto);
}
