package mission.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Channel implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final String firstId;

    private String name;
    private String oldName;
    private List<User> userList = new ArrayList<>();
    private Integer userCount;

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Channel(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        String idAsString = id.toString();
        this.firstId = idAsString.split("-")[0];
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }
    public List<User> getUserList() {
        return userList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Integer getUserCount() {
        return userCount = userList.size();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setName(String name) {
        this.name = name;
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id) && Objects.equals(firstId, channel.firstId) && Objects.equals(name, channel.name) && Objects.equals(userList, channel.userList) && Objects.equals(userCount, channel.userCount) && Objects.equals(createdAt, channel.createdAt) && Objects.equals(updatedAt, channel.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstId, name, userList, userCount, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "firstId='" + firstId + '\'' +
                ", name='" + name + '\'' +
                ", userCount=" + getUserCount() +
                '}';
    }
}