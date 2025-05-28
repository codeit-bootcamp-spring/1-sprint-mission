package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserDetails;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BasicUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public BasicUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("유저 없음"));
    return new UserDetails(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority(user.getRole().toString()))
    );
  }
}
