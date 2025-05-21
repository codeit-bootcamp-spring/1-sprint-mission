package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {
  private final User user;

  public CustomUserDetails(User user) {
    this.user = user;
  }


  // 사용자 계정 만료 여부 처리 필요 할 때 사용
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // 사용자 계정 잠김 여부 처리 필요 할 때 사용
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // 자격 증명 만료 여부 확인 용
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // 사용자 활성화 여부 처리 할 때 사용
  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(()-> "ROLE_USER");
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }
}
