package com.sprint.mission.discodeit.mapper;

import org.mapstruct.Mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

@Mapper(componentModel = "spring")
public interface BinaryContentMapper {

	BinaryContentDto toDto(BinaryContent binaryContent);
}
