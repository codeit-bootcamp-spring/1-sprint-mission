package com.spirnt.mission.discodeit.dto.userStatus;

import com.spirnt.mission.discodeit.enity.UserStatusType;

public record UserStatusUpdate(
        UserStatusType type
        // Instant lastSeenAt   // 클라이언트에서 받지 않고 서버에서 시간을 처리
) {
}
