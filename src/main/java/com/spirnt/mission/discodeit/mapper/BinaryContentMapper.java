package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

  BinaryContentDto toDto(BinaryContent binaryContent);
}
