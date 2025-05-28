package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Role;
import java.util.Collection;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private UUID id;
  private String username;
  private String email;
  private BinaryContentDto profile;
  private boolean online;
  private Collection<Role> roles;

  public void updateOnline(boolean online) {
    this.online = online;
  }
}
