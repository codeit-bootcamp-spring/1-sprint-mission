package com.sprint.mission.dto;

import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.UserDto;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import org.mapstruct.*;

import static org.mapstruct.MappingInheritanceStrategy.*;

@Mapper(componentModel = "spring")
public interface MessageMapper {

  @Mapping(target = "channelId", source = "channel.id")
  @Mapping(target = "author", source = "author", qualifiedByName = "toUserDto")
  MessageDto toDto(Message message);

  @Named("toUserDto")
  @Mapping(target = "online", expression = "java(user.getStatus() != null ? user.getStatus().isOnline() : null)")
  UserDto userToUserDto(User user);

  @Mapping(target = "channel", source = "channel")
  @Mapping(target = "author", source = "author")
  @Mapping(target = "content", source = "content")
  Message toEntity(Channel channel, User author, String content);
}
