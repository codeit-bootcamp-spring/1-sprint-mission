package discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private String introduction;
    private User owner;
    private List<User> participants;
    private List<Message> messages;

    public Channel(String name, String introduction, User owner) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        id = UUID.randomUUID();
        createdAt = currentUnixTime;
        updatedAt = currentUnixTime;

        this.name = name;
        this.introduction = introduction;
        this.owner = owner;
        participants = new ArrayList<>();
        messages = new ArrayList<>();
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
        updatedAt = System.currentTimeMillis() / 1000;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public User getOwner() {
        return owner;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void updateParticipants(User user) {
        participants.add(user);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void updateMessages(Message message) {
        messages.add(message);
    }

    public boolean isEquals(Channel channel) {
        return id.equals(channel.getId());
    }
}
