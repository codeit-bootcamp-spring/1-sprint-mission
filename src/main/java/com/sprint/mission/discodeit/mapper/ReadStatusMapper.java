package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReadStatusMapper {
    ReadStatusMapper INSTANCE = Mappers.getMapper(ReadStatusMapper.class);

    ReadStatusResponse toDto(ReadStatus readStatus);

    ReadStatus toEntity(ReadStatusRequest request);
}
