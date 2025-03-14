package com.sprint.mission.dto;

import com.sprint.mission.dto.response.MessageDto;
import com.sprint.mission.dto.response.PageResponse;
import com.sprint.mission.entity.main.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;

@Mapper
public interface PageResponseMapper {

    PageResponse<MessageDto> fromPage(Page<MessageDto> page);
}
