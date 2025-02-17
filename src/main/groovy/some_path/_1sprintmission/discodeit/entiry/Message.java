package some_path._1sprintmission.discodeit.entiry;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {

    private UUID sender;       // 메시지를 보낸 사용자
    private String content;    // 메시지 내용
    private Instant sentAt;       // 보낸 시간 (Unix timestamp)
    private UUID channel;


    public Message(UUID channelId, UUID senderId, String content) {
        super();
        this.sender = senderId;
        this.content = content;
        this.sentAt = Instant.from(LocalDateTime.now()); // 현재 시간
        this.channel = channelId;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + getId() +
                ", sender=" + sender.toString() +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}

