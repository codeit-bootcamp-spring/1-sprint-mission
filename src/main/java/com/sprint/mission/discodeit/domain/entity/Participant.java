package com.sprint.mission.discodeit.domain.entity;


import lombok.Getter;
import lombok.Setter;


import java.util.UUID;

@Getter
@Setter
public class Participant{
    private UUID id;
    private User user;
    private Channel channel;
    private Message message;

    public Participant(User user) {
        this.id = UUID.randomUUID();
        this.user = user;
    }
}
