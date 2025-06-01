package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // ROLE_USER, ROLE_ADMIN 등 권한이 있다면 꺼내서 전달
    return List.of(); // 지금은 권한 없이 빈 리스트
  }

  @Override
  public String getPassword() {
    return user.getPassword();  // 암호화된 비밀번호
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  // 아래 4가지 메서드의 반환값이 모드 true여야 로그인 가능
  @Override
  public boolean isAccountNonExpired() {  // 계정 만료 여부
    return true;
  }

  @Override
  public boolean isAccountNonLocked() { // 계정 잠김 여부
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {  // 비밀번호 만료 여부
    return true;
  }

  @Override
  public boolean isEnabled() {  // 계정 활성화 여부
    return true;
  }
}
