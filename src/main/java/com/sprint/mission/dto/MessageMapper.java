package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.BinaryContentDto;
import com.sprint.mission.dto.mappedDto.MessageDto;
import com.sprint.mission.dto.mappedDto.UserDto;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.entity.addOn.BinaryContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mapstruct.MappingInheritanceStrategy.*;

@MapperConfig(mappingInheritanceStrategy = AUTO_INHERIT_ALL_FROM_CONFIG)
@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "channelId", source = "channel.id")
    MessageDto toDto(Message message);

    @Mapping(target = "online", expression = "java(user.getStatus() != null ? user.getStatus().isOnline() : null)")
    UserDto userToUserDto(User user);

    @Mapping(target = "id", ignore = true) // 테스트용으로 ID SETTER 열어놔서 ignore 설정 필요
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "content", source = "responseDto.content")
    Message toEntity(MessageDtoForCreate responseDto, Channel channel, User author);

    Message update(MessageDtoForUpdate updateDto, @MappingTarget Message updatingMessage);
}
