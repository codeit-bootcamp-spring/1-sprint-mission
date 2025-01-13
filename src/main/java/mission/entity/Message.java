package mission.entity;

import java.util.Objects;
import java.util.UUID;

public class Message {


    private final UUID id;
    private final String firstId;
    private String message;
    private Long createAt;
    private Long updateAt;

    public Message(String message){
        this.message = message;
        id = UUID.randomUUID();
        String string = id.toString();
        firstId = string.split("-")[0];
        createAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setMessage(String message) {
        this.message = message;
        updateAt = System.currentTimeMillis();
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
