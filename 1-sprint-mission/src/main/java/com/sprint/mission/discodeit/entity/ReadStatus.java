package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private UUID id;
    private User user;
    private Channel channel;
    private Instant lastReadAt;


    public ReadStatus(User user, Channel channel) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void updateRedaTime() {
        this.lastReadAt = Instant.now();
    }

}
