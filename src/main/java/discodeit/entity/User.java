package discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private List<Channel> joinedChannels;

    public User(String name, String email, String phoneNumber, String password) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        id = UUID.randomUUID();
        createdAt = currentUnixTime;
        updatedAt = currentUnixTime;

        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        joinedChannels = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis() / 1000;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public List<Channel> getJoinedChannels() {
        return joinedChannels;
    }

    public void updateJoinedChannels(Channel channel) {
        joinedChannels.add(channel);
    }
}
