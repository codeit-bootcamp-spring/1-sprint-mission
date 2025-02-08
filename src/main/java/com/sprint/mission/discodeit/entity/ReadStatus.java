package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ReadStatus {
    //사용자가 채널 별 마지막으로 메시지를 읽은 시간 표현 -> 사용자별 각 채널의 읽지않은 메시지 확인
    private UUID id;
    private final UUID userId;

    //채널별 마지막 읽은 시간 저장
    private Map<UUID, Instant> channelLastReadTimes;

    private final Instant createdAt;
    private Instant updatedAt;

    public ReadStatus(UUID userId){
        this.id=UUID.randomUUID();
        this.userId=userId;
        this.createdAt=Instant.now();
    }
}
