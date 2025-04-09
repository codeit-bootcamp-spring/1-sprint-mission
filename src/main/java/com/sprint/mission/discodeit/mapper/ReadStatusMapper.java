package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

    public ReadStatusDto toDto(ReadStatus readStatus) {
        return ReadStatusDto.builder()
                .id(readStatus.getId())
                .channelId(readStatus.getChannel().getId())
                .userId(readStatus.getUser().getId())
                .build();
    }
}
