package com.sprint.mission.dto;

import com.sprint.mission.dto.mappedDto.BinaryContentDto;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.entity.addOn.BinaryContent;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class BinaryContentMapperImpl implements BinaryContentMapper {

    @Override
    public BinaryContentDto toDto(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        UUID id = null;
        String fileName = null;
        Long size = null;
        String contentType = null;

        id = binaryContent.getId();
        fileName = binaryContent.getFileName();
        size = binaryContent.getSize();
        contentType = binaryContent.getContentType();

        byte[] bytes = null;

        BinaryContentDto binaryContentDto = new BinaryContentDto( id, fileName, size, contentType, bytes );

        return binaryContentDto;
    }

    @Override
    public BinaryContent toEntity(BinaryContentDtoForCreate request) {
        if ( request == null ) {
            return null;
        }

        BinaryContent binaryContent = new BinaryContent();

        binaryContent.setFileName( request.fileName() );
        binaryContent.setContentType( request.contentType() );
        binaryContent.setSize( request.size() );

        return binaryContent;
    }
}
