package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.UserDto;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class DiscodeitUserDetails implements UserDetails {

  private final UserDto user;
  private final String password;

  public DiscodeitUserDetails(UserDto user, String password) {
    this.user = user;
    this.password = password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_".concat(user.role().name())));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.user.username();
  }

  public UUID getId() {
    return this.user.id();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DiscodeitUserDetails that)) {
      return false;
    }
    return user.username().equals(that.user.username());
  }

  @Override
  public int hashCode() {
    return Objects.hash(user.username());
  }
}
