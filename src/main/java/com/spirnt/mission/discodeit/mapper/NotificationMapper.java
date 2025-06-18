package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.notification.NotificationDto;
import com.spirnt.mission.discodeit.entity.Notification;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "receiverId", source = "receiver.id")
    NotificationDto toDto(Notification notification);

    List<NotificationDto> toDto(List<Notification> notifications);
}
