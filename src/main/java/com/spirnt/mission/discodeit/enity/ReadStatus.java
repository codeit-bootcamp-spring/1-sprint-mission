package com.spirnt.mission.discodeit.enity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

// 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인
// '유저'가 '채널'에서 마지막으로 읽은 '메세지' 기록
@Getter
public class ReadStatus extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;


    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    // 마지막으로 읽은 메세지 업데이트
    public void update(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
        updateClass(lastReadAt);
    }

    @Override
    public String toString() {
        return "ReadStatus[UserID: " + this.userId +
                " ChannelID: " + this.channelId +
                " LastRead: " + this.lastReadAt + "]";
    }
}
