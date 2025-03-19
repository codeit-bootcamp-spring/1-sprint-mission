package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ChannelMapperHelper.class})
@Named("ChannelMapper")
public interface ChannelMapper {

  @Mapping(source = "lastMessageAt", target = "lastMessageAt")
  @Mapping(source = "participantIds", target = "participants", qualifiedByName = "mapUserById")
  ChannelDto toDto(Channel channel, List<UUID> participantIds,
      Instant lastMessageAt);

}
