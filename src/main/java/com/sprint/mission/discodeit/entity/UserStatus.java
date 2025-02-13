package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID userId;
    private Instant lastActiveAt;

    public boolean isOnline() {
        return lastActiveAt != null && lastActiveAt.isAfter(Instant.now().minusSeconds(300));
    }
}
