package mission.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Message {


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

    public Message(String message){
        this.message = message;
        id = UUID.randomUUID();
        firstId = id.toString().split("-")[0];
        createAt = LocalDateTime.now();
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
