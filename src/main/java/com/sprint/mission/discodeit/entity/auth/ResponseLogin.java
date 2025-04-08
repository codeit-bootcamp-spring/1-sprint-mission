package com.sprint.mission.discodeit.entity.auth;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ResponseLogin(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
) {

}
