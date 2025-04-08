package com.sprint.mission.discodeit.entity.user.dto;


import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import java.util.UUID;

public record UserUpdateResponse(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
) {

}
