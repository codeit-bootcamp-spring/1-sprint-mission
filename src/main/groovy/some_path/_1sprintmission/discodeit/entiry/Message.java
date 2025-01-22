package some_path._1sprintmission.discodeit.entiry;
import java.time.Instant;
import java.util.UUID;

public class Message extends BaseEntity {
    private final User sender;       // 메시지를 보낸 사용자
    private final String content;    // 메시지 내용
    private final long sentAt;       // 보낸 시간 (Unix timestamp)

    public Message(User sender, String content) {
        super();
        this.sender = sender;
        this.content = content;
        this.sentAt = Instant.now().getEpochSecond(); // 현재 시간
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public long getSentAt() {
        return sentAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + getId() +
                ", sender=" + sender.getUsername() +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}

