package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailService implements UserDetailsService {

    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userValidator.validateUserExistsByUserName(username);

        boolean online = false;
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (Objects.equals(((UserDetails) principal).getUsername(), user.getName())) {
                online = true;
            }
        }

        return new CustomUserDetails(userMapper.toUserDto(user, online), user.getPassword());
    }
}
