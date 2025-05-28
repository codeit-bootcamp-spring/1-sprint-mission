package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserDetailsService userDetailsService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  // UserDto정보로 토큰을 생성할 수 있다. (JwtSession을 같이 저장)
  public JwtSession generateTokens(UserDto userDto) {

    UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.username());

    // 1. JwtTokenProvider로 토큰 생성
    String accessToken = jwtTokenProvider.generateAccessToken(userDetails, userDto);
    String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails, userDto);

    User user = userRepository.findByUsername(userDto.username())
        .orElseThrow(() -> new NoSuchElementException("User not found"));

    // 2. JwtSession을 같이 저장
    JwtSession jwtSession = new JwtSession(user, accessToken, refreshToken);
    jwtSessionRepository.save(jwtSession);

    return jwtSession;
  }

  // 토큰의 유효성을 검사할 수 있다.
  public boolean validateToken(String token) {
    return jwtTokenProvider.validate(token);
  }

  //리프레시 토큰을 무효화할 수 있다.
  public void invalidateRefreshToken(String refreshToken) {
    //1. refreshToken 이 유효한지 검사
    if (!jwtTokenProvider.validate(refreshToken) || jwtTokenProvider.isTokenExpired(refreshToken)) {
      return;
    }
    //2. 리프레시 토큰 무효화 -> 리프레시 토큰, jwtSession 삭제
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new NoSuchElementException("Invalid refresh token"));

    jwtSessionRepository.delete(jwtSession);

  }

  // 리프레시 토큰을 활용해 엑세스 토큰을 재발급할 수 있다.
  public String reissueAccessTokens(String refreshToken) {
    //1. refreshToken 이 유효한지 검사
    if (!jwtTokenProvider.validate(refreshToken) || jwtTokenProvider.isTokenExpired(refreshToken)) {
      return null;
    }

    //2. 유효한 refreshToken이라면 db에서 검색
    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new NoSuchElementException("Invalid refresh token"));

    //3. 새로 refresh 발급
    User user = jwtSession.getUser();
    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

    String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails,
        userMapper.toDto(user));

    //4. 기존의 refresh 를 삭제하고 jwtSession 다시 저장
    jwtSession.setRefreshToken(newRefreshToken);
    jwtSessionRepository.save(jwtSession);

    return jwtSession.getAccessToken();
  }

}
