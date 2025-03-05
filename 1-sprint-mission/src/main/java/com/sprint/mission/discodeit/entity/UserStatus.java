package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 3697388637512630558L;
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean userOnOff;
}
