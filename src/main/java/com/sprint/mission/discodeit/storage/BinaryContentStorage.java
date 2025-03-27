package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

//바이너리 데이터의 저장/로드를 담당하는 컴포넌트
public interface BinaryContentStorage {

  //BinayContent의 UUID 키 정보를 바탕으로 byte[] 데이터를 저장함.
  UUID put(UUID uuid, byte[] bytes);

  //키 정보를 바탕으로 byte[] 데이터를 읽어 InputStream 타입으로 반환합니다.
  InputStream get(UUID uuid);

  //HTTP API로 다운로드 기능을 제공합니다. 파일을 다운받을 수 있는 응답을 제공함.
  ResponseEntity<?> download(BinaryContentDto binaryContentDto);

}
