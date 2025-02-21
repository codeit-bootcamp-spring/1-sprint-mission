package com.sprint.mission.discodeit.dto.user;

//패스워드 제외
public record ResponseUserDto(
        String username,
        String email,
        String phoneNumber,
        String address,
        boolean isOnline //사용자의 온라인 상태 정보
) {
}
