package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID uuID;
    private final Instant createdAtl;
    private Instant updatedAt;
    private User user;
    private Channel channel;
    private Instant lastReadTime;

    public ReadStatus(){
        this.uuID = UUID.randomUUID();
        this.createdAtl = Instant.now();
        this.updatedAt = this.createdAtl;
    }



}
