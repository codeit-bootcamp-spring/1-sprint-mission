package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BinaryContentMapper {

  BinaryContentMapper INSTANCE = Mappers.getMapper(BinaryContentMapper.class);

  BinaryContentResponse toBinaryContentResponse(BinaryContent binaryContent);

  List<BinaryContentResponse> toResponseList(List<BinaryContent> binaryContents);
}
