package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
//사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델
//사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
public class ReadStatus extends Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전

    private UUID userId;
    private UUID channelId;
    private Instant lastReadTime; // 마지막으로 읽은 시간

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadTime) {
        super(UUID.randomUUID(), Instant.now());
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadTime = lastReadTime;
    }
    //마지막으로 읽은 시간 업데이트 및 읽은 상태 변경
    public void updateLastReadTime() {
        this.lastReadTime = Instant.now();
        updateTimestamp();
    }
}
