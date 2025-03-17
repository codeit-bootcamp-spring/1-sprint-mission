package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ReadStatusMaper {
  ReadStatusMaper Instance= Mappers.getMapper(ReadStatusMaper.class);

  ReadStatusDto readStatusToDto(ReadStatus readStatus);

  ReadStatus dtoToReadStatus(ReadStatusDto readStatusDto);
}
