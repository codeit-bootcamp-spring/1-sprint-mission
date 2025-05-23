package com.sprint.mission.discodeit.entity;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // 사용자 권한 정보 반환
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  // SessionRegistry 세션 찾기
  @Override
  public boolean equals(Object o) {
    if (this == o) { // 같은 객체인지
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomUserDetails that = (CustomUserDetails) o;
    return Objects.equals(this.getUsername(), that.getUsername()); // username 으로 실질적인 비교
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getUsername());
  }
}
