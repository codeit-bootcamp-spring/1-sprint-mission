package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-13T09:40:47+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ReadStatusMapperImpl implements ReadStatusMapper {

    @Override
    public ReadStatusDto toDto(ReadStatus readStatus) {
        if ( readStatus == null ) {
            return null;
        }

        ReadStatusDto readStatusDto = new ReadStatusDto();

        readStatusDto.setId( readStatus.getId() );
        readStatusDto.setLastReadAt( readStatus.getLastReadAt() );

        return readStatusDto;
    }
}
