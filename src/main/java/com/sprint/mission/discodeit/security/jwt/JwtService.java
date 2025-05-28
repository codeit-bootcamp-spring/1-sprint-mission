package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.TokenPair;
import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.Optional;
import java.util.UUID;

public interface JwtService {

  String generateAccessToken(UserDto userDto);
  String generateRefreshToken();

  boolean isTokenValid(String token);
  UserDto extractUser(String accessToken);

  String reissueAccessToken(String refreshToken);
  void saveSession(String refreshToken, String accessToken, UserDto userDto);
  void invalidateSession(String refreshToken);
  Optional<String> findAccessTokenByRefreshToken(String refreshToken);
  TokenPair reissueTokens(String refreshToken);
  long getRefreshTokenValiditySeconds();
  void invalidateAllSessionsByUserId(UUID userId);

}
