package com.sprint.mission.discodeit.security.jwt;

import java.time.Instant;

import com.sprint.mission.discodeit.dto.data.UserDto;

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
