package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.status.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReadStatusMapper {

  ReadStatusMapper INSTANCE = Mappers.getMapper(ReadStatusMapper.class);

  ReadStatusResponse toReadStatusResponse(ReadStatus readStatus);
}
