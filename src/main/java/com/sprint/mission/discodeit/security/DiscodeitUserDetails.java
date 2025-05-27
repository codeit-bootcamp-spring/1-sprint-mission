package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {
  private final UserDto userDto;
  private final String password; //DTO에 password가 포함되지 않으므로 별도의 필드로 정의


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_".concat(userDto.role().name())));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userDto.username();
  }
}
