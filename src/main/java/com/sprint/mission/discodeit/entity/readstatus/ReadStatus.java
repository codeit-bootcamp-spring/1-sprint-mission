package com.sprint.mission.discodeit.entity.readstatus;

import com.sprint.mission.discodeit.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ReadStatus extends BaseEntity {
    /*
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
     */
    @Serial
    private static final long serialVersionUID = 15L;
    private final UUID userId;
    private final UUID channelId;


    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
    }

    public void update() {
        updateTimeStamp();
    }

    public boolean isRead(Instant messageSentTime) {
        return !getUpdatedAt().isAfter(messageSentTime);
    }

}
/*
내가 안 읽은 메시지 몇개?
내가 읽은 마지막 메시지 UUID를 기준으로?

 */