package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.UserDto;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtSessionRepository jwtSessionRepository;

  @Transactional
  public JwtToken generateToken(UserDto userDto) {
    String accessToken = jwtTokenProvider.generateAccessToken(userDto);
    String refreshToken = jwtTokenProvider.generateRefreshToken(userDto);

    JwtSession session = new JwtSession(userDto.id(), accessToken, refreshToken);
    jwtSessionRepository.save(session);

    return new JwtToken(accessToken, refreshToken);
  }

  @Transactional
  public JwtToken refresh(String refreshToken) {
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new NoSuchElementException());

    if (!jwtTokenProvider.validateToken(refreshToken)) {
      throw new IllegalArgumentException();
    }

    UserDto userDto = jwtTokenProvider.getUserDtoFromToken(refreshToken);

    String newAccessToken = jwtTokenProvider.generateAccessToken(userDto);
    String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDto);

    session.update(newAccessToken, newRefreshToken);

    return new JwtToken(newAccessToken, newRefreshToken);
  }

  @Transactional
  public void revoke(String refreshToken) {
    jwtSessionRepository.deleteByRefreshToken(refreshToken);
  }

  public boolean validateToken(String token) {
    return jwtTokenProvider.validateToken(token);
  }
}
