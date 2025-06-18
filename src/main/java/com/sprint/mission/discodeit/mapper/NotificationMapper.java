package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.event_entity.NotificationEvent;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NotificationMapper {


  @Mapping(target = "receiverId", source = "receiver.id")
  NotificationDto toDto(Notification notification);

  List<NotificationDto> toDtoList(List<Notification> notifications);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "receiver", source = "receiver")
  @Mapping(target = "type", source = "event.type")
  @Mapping(target = "targetId", source = "event.targetId")
  @Mapping(target = "title", source = "event.title")
  @Mapping(target = "content", source = "event.content")
  Notification toEntityFromEvent(NotificationEvent event, User receiver);
}
