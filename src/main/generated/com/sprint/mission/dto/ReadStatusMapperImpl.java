package com.sprint.mission.dto;

import com.sprint.mission.dto.response.ReadStatusDto;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.ReadStatus;
import com.sprint.mission.entity.User;
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

        UUID userId = null;
        UUID channelId = null;
        Instant lastReadAt = null;
        UUID id = null;

        userId = readStatusUserId( readStatus );
        channelId = readStatusChannelId( readStatus );
        lastReadAt = readStatus.getLastReadAt();
        id = readStatus.getId();

        ReadStatusDto readStatusDto = new ReadStatusDto( id, userId, channelId, lastReadAt );

        return readStatusDto;
    }

    private UUID readStatusUserId(ReadStatus readStatus) {
        User user = readStatus.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private UUID readStatusChannelId(ReadStatus readStatus) {
        Channel channel = readStatus.getChannel();
        if ( channel == null ) {
            return null;
        }
        return channel.getId();
    }
}
