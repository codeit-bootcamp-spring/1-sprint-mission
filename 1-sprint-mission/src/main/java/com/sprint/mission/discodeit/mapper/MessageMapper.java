package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring", uses = {UserMapper.class, MessageMapperHelper.class})
@Named("MessageMapper")
public interface MessageMapper {

  @Mapping(source = "attachmentIds", target = "attachments", qualifiedByName = "mapBinaryContents")
  MessageDto toDto(Message message);

}
