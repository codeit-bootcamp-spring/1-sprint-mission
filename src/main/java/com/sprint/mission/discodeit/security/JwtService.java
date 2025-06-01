package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.data.UserDto;

public interface JwtService {
  JwtSession generateTokens(UserDto userDto);
  boolean validateAccessToken(String token);
  UserDto parseUserFromAccessToken(String token);

  JwtSession rotateRefreshToken(String refreshToken);
  void invalidateRefreshToken(String refreshToken);
  JwtSession findByRefreshToken(String refreshToken);
}
