package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ReadStatus {
    //사용자가 채널 별 마지막으로 메시지를 읽은 시간 표현 -> 사용자별 각 채널의 읽지않은 메시지 확인
    private UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final UUID channelId;
    private final UUID userId;
    private Instant channelLastReadTimes;//채널별 마지막 읽은 시간 저장


    public ReadStatus(UUID channelId, UUID userId){
        this.id=UUID.randomUUID();
        this.createdAt=Instant.now();
        this.updatedAt=createdAt;

        this.userId=userId;
        this.channelId=channelId;
    }

    public void update() {
        this.channelLastReadTimes=Instant.now();
    }
}
