package discodeit.enity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private UUID senderId;
    private UUID channelId;


    public Message(UUID senderId, UUID channelId, String content) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    //Getter
    public String getContent() {
        return content;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    // Update
    public void updateContent(String content) {
        if (content == null || content.equals(this.content)) return;
        this.content = content;
        updateClass(System.currentTimeMillis());
    }

    // 객체 직렬화 부분에서 ser 파일에 저장할 형식으로 추가
    @Override
    public String toString() {
        return "Message{" +
                "id='" + this.getId() + '\'' +
                ", content='" + this.content + '\'' +
                '}';
    }

    // 객체를 UUID로 비교하기 위해
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Message message = (Message) obj;
        return Objects.equals(this.getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    // Read 시 출력용
    public String messageInfoToString() {
        return content +
                (this.getCreatedAt() == this.getUpdatedAt()
                        ? " (time: " + this.getCreatedAt() + ")"
                        : " (time: " + this.getUpdatedAt() + " (수정됨))");
    }
}
