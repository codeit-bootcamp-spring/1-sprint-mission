package com.sprint.mission.dto;

import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.PageResponse;
import com.sprint.mission.dto.response.ScrollPageResponse;
import com.sprint.mission.entity.main.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.EntityGraph;

@Mapper
public interface PageResponseMapper {

    PageResponse<MessageDto> fromPage(Page<MessageDto> page);

    @Mapping(target = "content", expression = "java(window.getContent())")
    @Mapping(target = "nextCursor", source = "nextCursor")
    @Mapping(target = "size", expression = "java(window.size())")
    @Mapping(target = "hasNext", expression = "java(window.hasNext())")
    @Mapping(target = "totalElements", source = "totalElements")
    ScrollPageResponse<MessageDto> fromScrollPage(Window<MessageDto> window, ScrollPosition nextCursor,Long totalElements);
}
