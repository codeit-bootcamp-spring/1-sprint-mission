package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    public User getUser() {
        return user;
    }

    public UserDto toDto() {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null, // BinaryContentDto는 생략하거나 직접 매핑
                user.getStatus().isOnline()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 정보가 없다면 빈 컬렉션 반환
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 기본 true 처리
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 기본 true 처리
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 기본 true 처리
    }

    @Override
    public boolean isEnabled() {
        return true; // 기본 true 처리
    }
}