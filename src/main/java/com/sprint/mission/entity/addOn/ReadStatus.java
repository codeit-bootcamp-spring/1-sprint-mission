package com.sprint.mission.entity.addOn;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReadStatus {

    //사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현
    //사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용
    // 채널요청 시 여기에 저장되게??
    // 채널 ID,
    Map<UUID, Instant> lastReadChannel = new HashMap<>();

    public void updateReadTime(UUID channelId){
        lastReadChannel.replace(channelId, Instant.now());
    }

    public Instant findLastReadByChannel(UUID channelId){
        return lastReadChannel.get(channelId);
    }
}
