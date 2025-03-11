package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.ReadStatusDto;
import com.sprint.mission.entity.addOn.ReadStatus;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class ReadStatusMapperImpl implements ReadStatusMapper {

    @Override
    public ReadStatusDto toDto(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }

        UUID id = null;
        Instant lastReadAt = null;

        id = readStatus.getId();
        lastReadAt = readStatus.getLastReadAt();

        UUID userId = null;
        UUID channelId = null;

        ReadStatusDto readStatusDto = new ReadStatusDto( id, userId, channelId, lastReadAt );

        return readStatusDto;
    }
}
