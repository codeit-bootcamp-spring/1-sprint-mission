package some_path._1sprintmission.discodeit.entiry;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
public class UserStatus extends BaseEntity implements Serializable {

    private User user;

    private Instant lastSeenAt; // Instant 타입 사용

    public UserStatus(User user, Instant lastSeenAt) {
        this.user = user;
        this.lastSeenAt = lastSeenAt;
    }

    public void updateLastSeenAt(Instant newLastSeenAt) {
        this.lastSeenAt = newLastSeenAt;
    }

    public boolean isOnline() {
        return Instant.now().minus(Duration.ofMinutes(5)).isBefore(this.lastSeenAt);
    }
}
