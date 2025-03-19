package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-13T10:25:17+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class BinaryContentMapperImpl implements BinaryContentMapper {

    @Override
    public BinaryContentDto toDto(BinaryContent binaryContent) {
        if ( binaryContent == null ) {
            return null;
        }

        BinaryContentDto binaryContentDto = new BinaryContentDto();

        binaryContentDto.setId( binaryContent.getId() );
        binaryContentDto.setFileName( binaryContent.getFileName() );
        binaryContentDto.setSize( binaryContent.getSize() );
        binaryContentDto.setContentType( binaryContent.getContentType() );

        return binaryContentDto;
    }

    @Override
    public BinaryContent toEntity(BinaryContentDto dto) {
        if ( dto == null ) {
            return null;
        }

        BinaryContent binaryContent = new BinaryContent();

        binaryContent.setId( dto.getId() );
        binaryContent.setFileName( dto.getFileName() );
        binaryContent.setSize( dto.getSize() );
        binaryContent.setContentType( dto.getContentType() );

        return binaryContent;
    }

    @Override
    public List<BinaryContentDto> toDtoList(List<BinaryContent> binaryContentList) {
        if ( binaryContentList == null ) {
            return null;
        }

        List<BinaryContentDto> list = new ArrayList<BinaryContentDto>( binaryContentList.size() );
        for ( BinaryContent binaryContent : binaryContentList ) {
            list.add( toDto( binaryContent ) );
        }

        return list;
    }
}
