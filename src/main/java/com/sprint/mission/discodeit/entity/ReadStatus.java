package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ReadStatus extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID userId;
    private final UUID channelId;
    private Instant lastActiveAt;

    public ReadStatus(ReadStatusDTO dto) {
        super();
        this.userId = dto.userId();
        this.channelId = dto.channelId();
        this.lastActiveAt = dto.lastActiveAt();
    }
    public void update(Instant time) {
        this.lastActiveAt = time;
        updateTime(time);
    }
}
