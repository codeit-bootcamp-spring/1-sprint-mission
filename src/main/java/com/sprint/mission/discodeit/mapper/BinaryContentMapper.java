package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BinaryContentMapper {

  @PostConstruct
  public void init() {
    // 메퍼가 초기화되었는지, binaryContentStorage가 주입되었는지 확인
    // System.out.println(" BinaryContentMapper  BinaryContentMapper  BinaryContentMapper ");
  }

  @Autowired
  protected BinaryContentStorage binaryContentStorage;

  // BinaryContent에 bytes 없고, id로 BinaryContentStorage에서 들고와야 한다.
  @Mapping(target = "bytes", source = "id", qualifiedByName = "mapBytes")
  public abstract BinaryContentDto toDto(BinaryContent binaryContent);

  // 맞는지 모르겠음 우선 보류
  public abstract BinaryContent toEntity(BinaryContentDto binaryContentDto);

  // 5.10 @Named 어노테이션 - Mapping#qualitiedByName 연계
  // Mapping#qualifiedByName또는 를 제공하면 Mapping#qualifiedByMapStruct가 해당 메서드를 사용하도록 강제합니다.
  @Named("mapBytes")
  byte[] mapBytes(UUID id) {
    try (InputStream inputStream = binaryContentStorage.get(id)) {  // 스트림을 닫도록 설정
      return inputStream.readAllBytes();  // 한 번만 읽고, 데이터를 바이트 배열로 반환
    } catch (IOException e) {
      throw new RuntimeException("Failed to read binary content for ID: " + id, e);
    }
  }

}
