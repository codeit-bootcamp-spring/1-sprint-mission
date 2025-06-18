package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

    public ReadStatusDto toDto(ReadStatus entity) {
        if (entity == null) {
            return null;
        }
        ReadStatusDto dto = new ReadStatusDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setChannelId(entity.getChannel().getId());
        dto.setLastReadAt(entity.getLastReadAt());
        return dto;
    }
}
