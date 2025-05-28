package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.security.role.Role;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponseDto(UUID id, String username, String nickname, String email, Role role,
                              BinaryContentResponseDto profile,
                              Boolean online) {

}
