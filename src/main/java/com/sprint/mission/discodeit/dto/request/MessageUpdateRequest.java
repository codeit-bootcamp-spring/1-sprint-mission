package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record MessageUpdateRequest(
    String newContent,
    UUID requesterId // TODO찐: API 스펙대로 빼야하나
) {

}
