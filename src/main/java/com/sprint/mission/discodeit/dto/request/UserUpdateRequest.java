package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
	@Size(max = 50, message = "새 사용자 이름은 최대 50자까지 가능합니다.")
	String newUsername,

	@Email(message = "유효한 이메일 주소여야 합니다.")
	@Size(max = 100, message = "새 이메일은 최대 100자까지 가능합니다.")
	String newEmail,

	@Size(min = 8, max = 60, message = "새 비밀번호는 8~60자여야 합니다.")
	String newPassword
) {
}