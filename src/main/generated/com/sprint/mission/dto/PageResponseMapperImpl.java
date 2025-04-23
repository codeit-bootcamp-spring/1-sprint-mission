package com.sprint.mission.dto;

import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.PageResponse;
import com.sprint.mission.dto.response.ScrollPageResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class PageResponseMapperImpl implements PageResponseMapper {

    @Override
    public PageResponse<MessageDto> fromPage(Page<MessageDto> page) {
        if ( page == null ) {
            return null;
        }

        List<MessageDto> content = null;
        int number = 0;
        int size = 0;
        Long totalElements = null;

        if ( page.hasContent() ) {
            List<MessageDto> list = page.getContent();
            content = new ArrayList<MessageDto>( list );
        }
        number = page.getNumber();
        size = page.getSize();
        totalElements = page.getTotalElements();

        boolean hasNext = false;

        PageResponse<MessageDto> pageResponse = new PageResponse<MessageDto>( content, number, size, hasNext, totalElements );

        return pageResponse;
    }

    @Override
    public ScrollPageResponse<MessageDto> toScrollPageResponse(Window<MessageDto> window, ScrollPosition nextCursor, Long totalElements) {
        if ( window == null && nextCursor == null && totalElements == null ) {
            return null;
        }

        Object nextCursor1 = null;
        nextCursor1 = nextCursor;
        Long totalElements1 = null;
        totalElements1 = totalElements;

        List<MessageDto> content = window.getContent();
        int size = window.size();
        boolean hasNext = window.hasNext();

        ScrollPageResponse<MessageDto> scrollPageResponse = new ScrollPageResponse<MessageDto>( content, nextCursor1, size, hasNext, totalElements1 );

        return scrollPageResponse;
    }
}
