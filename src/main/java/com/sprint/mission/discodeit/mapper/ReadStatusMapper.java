package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

    public ReadStatusDto readStatusEntityToDto(ReadStatus readStatus) {
        return new ReadStatusDto(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastReadAt(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt()
        );
    }
}
