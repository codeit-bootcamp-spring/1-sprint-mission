package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

// ReadStatus: 사용자의 채널별 마지막 메시지 읽은 시간 관리
public class ReadStatus extends BaseEntity {
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
}
