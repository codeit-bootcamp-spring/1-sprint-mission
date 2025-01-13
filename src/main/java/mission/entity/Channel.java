package mission.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Channel {

    private final UUID id;
    private final String firstId;

    private String name;
    private List<User> userList = new ArrayList<>();

    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Channel(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        String idAsString = id.toString();
        this.firstId = idAsString.split("-")[0];
        this.createdAt = LocalDateTime.now();
    }

    // 채널에서 user 등록
    public void joinUser(User user){
        userList.add(user);
        user.getChannels().add(this);
    }

    // 유저 강퇴
    public void deleteUser(User user){
        userList.remove(user);
        user.getChannels().remove(this);
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
        return Objects.equals(id, channel.id);
                //&& Objects.equals(name, channel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {

        return "[" + firstId + "] " + "Channel{'" + name + '\'' + '}';
    }
}