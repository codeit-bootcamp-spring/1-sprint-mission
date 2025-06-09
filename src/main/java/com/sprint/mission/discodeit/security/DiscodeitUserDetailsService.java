package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscodeitUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    com.sprint.mission.discodeit.entity.user.User findUser = userRepository.findUserByUsername(
        username);

    if (findUser == null) {
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
          Map.of(username, ErrorCode.USER_NOT_FOUND.getMessage()));
    }
    
    return new DiscodeitUserDetails(findUser);
  }

}
