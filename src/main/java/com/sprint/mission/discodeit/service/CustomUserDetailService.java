package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserValidator userValidator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userValidator.validateUserExistsByUserName(username);
        return new CustomUserDetails(user);
    }
}
