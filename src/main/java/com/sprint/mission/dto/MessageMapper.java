package com.sprint.mission.dto;

import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.UserDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import org.mapstruct.*;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "channelId", source = "channel.id")
    @Mapping(target = "author", source = "author", qualifiedByName = "toUserDto")
    MessageDto toDto(Message message);

    @Named("toUserDto")
    @Mapping(target = "online", expression = "java(user.getStatus() != null ? user.getStatus().isOnline() : null)")
    UserDto userToUserDto(User user);

    Message toEntity(Channel channel, User author, String content);

    //Message update(MessageDtoForUpdate updateDto, @MappingTarget Message updatingMessage);
}
