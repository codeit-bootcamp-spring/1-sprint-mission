package mission.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String firstId;
    private String message;

    private User writer;
    private Channel writedAt;

    public Channel getWritedAt() {
        return writedAt;
    }

    public void setWritedAt(Channel writedAt) {
        this.writedAt = writedAt;
    }

    private final LocalDateTime createAt;
    private LocalDateTime updateAt;

    // 무조건 메시지는 CREATE로 생성하도록
    private Message(String message) {
        this.message = message;
        id = UUID.randomUUID();
        firstId = id.toString().split("-")[0];
        createAt = LocalDateTime.now();
    }

    public static Message createMessage(Channel channel, User user, String message) {
        Message mess = new Message(message);
        mess.setWriter(user);
        mess.setWritedAt(channel);
        user.getMessages().add(mess);
        channel.getMessageList().add(mess);
        return mess;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setMessage(String message) {
        this.message = message;
        updateAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + firstId + "]" + "Message{" +
                "message='" + message + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(id, message1.id) && Objects.equals(message, message1.message) && Objects.equals(createAt, message1.createAt) && Objects.equals(updateAt, message1.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, createAt, updateAt);
    }
}
