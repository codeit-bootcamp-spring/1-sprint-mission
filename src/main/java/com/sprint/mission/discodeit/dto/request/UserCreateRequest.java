package com.sprint.mission.discodeit.dto.request;

public record UserCreateRequest(
        String username,
        String email,
        String password) {
}
// 회원가입시 입력받은 정보
// 회원 등록시 저장할 정보만을 DTO화