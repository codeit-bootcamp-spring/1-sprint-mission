package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReadStatusMapper {
    ReadStatusMapper INSTANCE = Mappers.getMapper(ReadStatusMapper.class);

    ReadStatusDto toDto(ReadStatus readStatus);
}
