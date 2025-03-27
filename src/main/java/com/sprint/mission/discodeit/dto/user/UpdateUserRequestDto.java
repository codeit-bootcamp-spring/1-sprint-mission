package com.sprint.mission.discodeit.dto.user;

public record UpdateUserRequestDto(String email, String password, String name, String nickname, String phoneNumber) {
}
