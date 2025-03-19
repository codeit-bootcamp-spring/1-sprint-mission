package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface BinaryContentStorage {

  UUID put(UUID id, byte[] bytes); //UUID 키 정보를 바탕으로 byte[] 데이터를 저장합니다.

  InputStream get(UUID id); //키 정보를 바탕으로 byte[] 데이터를 읽어 InputStream 타입으로 반환합니다.

  ResponseEntity<?> download(BinaryContentDto binaryContentDto); //HTTP API로 다운로드 기능을 제공합니다.
}
