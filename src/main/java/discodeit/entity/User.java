package discodeit.entity;

import org.mindrot.jbcrypt.BCrypt;

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
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
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

    public void updatePassword(String originalPassword, String newPassword) {
        if (!BCrypt.checkpw(originalPassword, password)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    public List<Channel> getJoinedChannels() {
        return joinedChannels;
    }

    public void updateJoinedChannels(Channel channel) {
        joinedChannels.add(channel);
    }

    public void deleteJoinedChannel(Channel channel) {
        Channel deleteChannel = findChannel(channel);
        if (deleteChannel == null) {
            throw new IllegalArgumentException("가입되지 않은 채널입니다.");
        }
        joinedChannels.remove(deleteChannel);
    }

    public Channel findChannel(Channel channel) {
        return joinedChannels.stream()
                .filter(joinedChannel -> joinedChannel.isEquals(channel))
                .findAny()
                .orElse(null);
    }

    @Override
    public String toString() {
        return String.format(
                name + "님의 정보입니다." + System.lineSeparator()
                        + "Name: " + name + System.lineSeparator()
                        + "Email: " + email + System.lineSeparator()
                        + "Phone number: " + phoneNumber + System.lineSeparator()
                        + "Joined Channels: "
                        + String.join(", ", joinedChannels.stream()
                        .map(Channel::getName)
                        .toList())
        );
    }
}
