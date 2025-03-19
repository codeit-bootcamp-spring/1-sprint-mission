package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Named("MessageMapperHelper")
public class MessageMapperHelper {

  BinaryContentRepository binaryContentRepository;
  BinaryContentMapper binaryContentMapper;

  @Autowired
  public MessageMapperHelper(BinaryContentRepository binaryContentRepository,
      BinaryContentMapper binaryContentMapper) {
    this.binaryContentRepository = binaryContentRepository;
    this.binaryContentMapper = binaryContentMapper;
  }


  @Named("mapBinaryContents")
  public List<BinaryContentDto> mapBinaryContents(List<UUID> attachmentIds) {
    if (attachmentIds == null) {
      return new ArrayList<>();
    }

    List<BinaryContentDto> list = new ArrayList<>();

    for (UUID id : attachmentIds) {
      list.add(binaryContentRepository.findById(id)
          .map(binaryContentMapper::toDto)
          .orElse(null));
    }

    return list;
  }

}
