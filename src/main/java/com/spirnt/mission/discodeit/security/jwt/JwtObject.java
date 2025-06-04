package com.spirnt.mission.discodeit.security.jwt;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import java.time.Instant;

/**
 * 생성된 토큰 정보를 나타내는 객체
 */
public record JwtObject(
    Instant issueTime,
    Instant expirationTime,
    UserDto userDto,
    String token
) {

    public boolean isExpired() {
        return expirationTime.isBefore(Instant.now());
    }
}
