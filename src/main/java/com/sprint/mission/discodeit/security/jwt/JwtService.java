package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserDetailsService userDetailsService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Transactional
  // UserDto정보로 토큰을 생성할 수 있다. (JwtSession을 같이 저장)
  public JwtSession generateTokens(UserDto userDto) {

    UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.username());

    // 0. 이미 해당 유저의 토큰이 존재하는지 조회
    User user = userRepository.findByUsername(userDto.username())
        .orElseThrow(() -> new NoSuchElementException("User not found"));

    // 기존 JWT 세션들을 모두 삭제
    jwtSessionRepository.deleteByUser(user);

    // 1. JwtTokenProvider로 토큰 생성
    String accessToken = jwtTokenProvider.generateAccessToken(userDetails, userDto);
    String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails, userDto);

    // 2. JwtSession을 같이 저장
    JwtSession jwtSession = new JwtSession(user, accessToken, refreshToken,
        jwtTokenProvider.getExpiration(accessToken).toInstant());
    jwtSessionRepository.save(jwtSession);

    return jwtSession;
  }

  // 토큰의 유효성을 검사할 수 있다.
  public boolean validateToken(String token) {
    return jwtTokenProvider.validate(token);
  }

  @Transactional
  //리프레시 토큰을 무효화할 수 있다.
  public void invalidateRefreshToken(String refreshToken) {
    //1. refreshToken 이 유효한지 검사
    if (!jwtTokenProvider.validate(refreshToken) || jwtTokenProvider.isTokenExpired(refreshToken)) {
      return;
    }
    //2. 리프레시 토큰 무효화 -> 리프레시 토큰, jwtSession 삭제
    JwtSession jwtSession = jwtSessionRepository.findJwtSessionByRefreshToken(refreshToken)
        .orElseThrow(() -> new NoSuchElementException("Invalid refresh token"));

    jwtSessionRepository.delete(jwtSession);

  }

  @Transactional
  // 리프레시 토큰을 활용해 엑세스 토큰을 재발급할 수 있다.
  public String reissueAccessTokens(String refreshToken) {
    //1. refreshToken 이 유효한지 검사

    log.info("reissueAccessTokens 호출. refreshToken: {}", refreshToken);

    if (!jwtTokenProvider.validate(refreshToken) || jwtTokenProvider.isTokenExpired(refreshToken)) {
      log.debug("토큰이 유효하지 않음 또는 만료됨");
      return null;
    }

    //2. 유효한 refreshToken이라면 db에서 검색
    JwtSession jwtSession = jwtSessionRepository.findJwtSessionByRefreshToken(refreshToken)
        .orElseThrow(
            () -> new NoSuchElementException("Invalid refresh token")
        );

    //3. 새로 refresh, access 발급
    User user = jwtSession.getUser();
    UserDto userDto = userMapper.toDto(user);
    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

    String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails, userDto);
    String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails, userDto);

    //4. 기존의 access, refresh 를 삭제하고 jwtSession 다시 저장
    jwtSession.update(newAccessToken, newRefreshToken);
    jwtSessionRepository.save(jwtSession);

    return jwtSession.getAccessToken();
  }

}
