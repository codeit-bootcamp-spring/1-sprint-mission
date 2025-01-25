package discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String content;
    private User sender;
    private UUID channelId;

    public Message(String content, User sender, UUID channelId) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        this.id = UUID.randomUUID();
        this.createdAt = currentUnixTime;
        this.updatedAt = currentUnixTime;

        this.content = content;
        this.sender = sender;
        this.channelId = channelId;
    }

    public UUID getId() {
        return id;
    }

    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis() / 1000;
    }

    public void updateContent(String content) {
        if (!this.content.equals(content)) {
            this.content = content;
            updateUpdatedAt();
        }
    }

    public void checkSender(User user) {
        if (!sender.isIdEqualTo(user.getId())) {
            throw new IllegalArgumentException("자신의 메시지만 삭제할 수 있습니다.");
        }
    }

    public boolean isIdEqualTo(UUID id) {
        return this.id.equals(id);
    }

    @Override
    public String toString() {
        return String.format(sender.getName() + ": " + content);
    }
}
