package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// componentModel 지정 시, 생성된 매퍼를 특정 DI 컨테이너에서 관리하도록 설정 가능
// -> Mapper.INSTANCE와 같은 방식으로 호출 불필요
@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentDto toDto(BinaryContent binaryContent);
}
