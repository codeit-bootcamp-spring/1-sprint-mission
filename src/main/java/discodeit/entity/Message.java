package discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String content;
    private User sender;

    public Message(String content, User sender) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        id = UUID.randomUUID();
        createdAt = currentUnixTime;
        updatedAt = currentUnixTime;

        this.content = content;
        this.sender = sender;
    }

    public UUID getId() {
        return id;
    }

    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis() / 1000;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void checkSender(User user) {
        if (!sender.isEqualTo(user)) {
            throw new IllegalArgumentException("자신의 메시지만 삭제할 수 있습니다.");
        }
    }

    public boolean isEqualTo(Message message) {
        return id.equals(message.getId());
    }

    public boolean isIdEqualTo(UUID id) {
        return this.id.equals(id);
    }

    @Override
    public String toString() {
        return String.format(sender.getName() + ": " + content);
    }
}
