package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
import java.time.Instant;

// ✅ UserUpdateDTO (사용자 정보 수정 시 필요한 데이터)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private UUID id;
    private String username;
    private String email;
    private UUID profileImageId; // 선택적으로 프로필 이미지 수정 가능
}