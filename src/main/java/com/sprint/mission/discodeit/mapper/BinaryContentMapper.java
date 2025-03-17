package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface BinaryContentMapper {
  BinaryContentMapper instance= Mappers.getMapper(BinaryContentMapper.class);

  BinaryContentDto binaryContentToDto(BinaryContent b);

  BinaryContent dtoToBinaryContent(BinaryContentDto b);
}
