package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateDTO {
    private String username;
    private String email;
    private UUID profileImageId;
    private String password; // ✅ 추가

    // ✅ 기본 생성자 추가 (Lombok으로 자동 생성)
}
