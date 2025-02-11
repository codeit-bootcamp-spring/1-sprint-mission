package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;
import java.time.Instant;

import java.util.UUID;

// ✅ UserReadDTO (사용자 조회 시 필요한 데이터)
@Getter
@Setter
@AllArgsConstructor
public class UserReadDTO {
    private UUID id;
    private String username;
    private String email;
    private UUID profileImageId;
    private boolean isOnline; // 온라인 상태 포함
    private Instant lastActive; // 마지막 접속 시간 포함
}
