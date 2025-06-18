package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.security.role.Role;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
public class UserResponseDto {

  private final UUID id;
  private final String username;
  private final String nickname;
  private final String email;
  private final Role role;
  private final BinaryContentResponseDto profile;
  private final Boolean online;

  @Builder
  private UserResponseDto(UUID id, String username, String nickname, String email, Role role,
      BinaryContentResponseDto profile, Boolean online) {
    this.id = id;
    this.username = username;
    this.nickname = nickname;
    this.email = email;
    this.role = role;
    this.profile = profile;
    this.online = online;
  }
}
