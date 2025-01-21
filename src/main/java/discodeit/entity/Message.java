package discodeit.entity;


import java.util.UUID;

public class Message extends Base {
    private String content;
    private UUID channelId;
    private UUID authorId;

    // 생성자
    public Message(String content, UUID channelId, UUID authorId) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    // Getter
    public String getContent() {
        return content;
    }
    public UUID getChannelId() {
        return channelId;
    }
    public UUID getAuthorId() {
        return authorId;
    }

    // Update
    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if(newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if(anyValueUpdated) {
            this.updateUpdatedAt();
        }
    }
}
