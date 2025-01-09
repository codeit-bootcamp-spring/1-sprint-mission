package mission1.entity;

import java.util.Objects;
import java.util.UUID;

public class Channel {

    private final UUID id;
    private final String firstId;

    private String name;
    private final Long createAt;
    private Long updateAt;

    public Channel(String name) {
        this.name = name;
        id = UUID.randomUUID();
        String string = id.toString();
        firstId = string.split("-")[0];
        createAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setName(String name) {
        this.name = name;
        updateAt = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id) && Objects.equals(name, channel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {

        return "[" + firstId + "] " + "Channel{'" + name + '\'' + '}';
    }
}