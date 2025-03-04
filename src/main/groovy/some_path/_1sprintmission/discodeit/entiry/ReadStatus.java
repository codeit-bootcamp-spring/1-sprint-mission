package some_path._1sprintmission.discodeit.entiry;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class ReadStatus extends BaseEntity implements Serializable {
    private User user;

    private Channel channel;

    private Instant lastReadAt; // Instant 타입 사용

    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastReadAt(Instant newReadTime) {
        this.lastReadAt = newReadTime;
    }
}
