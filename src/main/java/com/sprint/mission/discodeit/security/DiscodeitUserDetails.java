package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.User;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class DiscodeitUserDetails implements UserDetails {

  private final User user;

  public DiscodeitUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(user.getRole());
  }

  @Override
  public String getPassword() {
    return this.user.getPassword();
  }

  @Override
  public String getUsername() {
    return this.user.getUsername();
  }

  public UUID getId() {
    return this.user.getId();
  }
}
