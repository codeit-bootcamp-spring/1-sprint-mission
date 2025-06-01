package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.response.UserResponse;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserResponse userResponse;
    private final String password;

    public UUID getId() {
        return userResponse.id();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userResponse.role()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userResponse.username();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomUserDetails that)) {
            return false;
        }
        return userResponse.username().equals(that.userResponse.username());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userResponse.username());
    }
}
