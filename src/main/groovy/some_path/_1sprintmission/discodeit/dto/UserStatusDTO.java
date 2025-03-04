
package some_path._1sprintmission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public class UserStatusDTO {
    private UUID userId;
    private Instant lastSeenAt;

    public UserStatusDTO(UUID userId, Instant lastSeenAt) {
        this.userId = userId;
        this.lastSeenAt = lastSeenAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }
}

