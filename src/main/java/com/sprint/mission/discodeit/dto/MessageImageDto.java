package com.sprint.mission.discodeit.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record MessageImageDto(
    List<List<Object>> fileInfoList
) {

}
