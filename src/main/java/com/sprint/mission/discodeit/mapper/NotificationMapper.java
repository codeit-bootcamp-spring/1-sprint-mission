package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.sprint.mission.discodeit.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  @Mapping(target = "receiverId", source = "receiver.id")
  NotificationDto toDto(Notification notification);
}
