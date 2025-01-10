package discodeit.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Channel {
    private final String channelId;
    private String channelName;
    private final long createdAt;
    private long updatedAt;
    private Map<String, User> users;

    public Channel(String channelName) {
        channelId = UUID.randomUUID().toString();
        this.channelName = channelName;
        createdAt = System.currentTimeMillis();
        users = new HashMap<>();
    }

    // Getter
    public String getChannelId() { return channelId; }

    public String getChannelName() { return channelName; }

    public User getUser(String userId) { return users.get(userId); }

    public Map<String, User> getUsers() { return users; }

    public long getCreatedAt() { return createdAt; }

    public long getUpdatedAt() { return updatedAt; }

    // Setter
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
        this.updatedAt = System.currentTimeMillis();
    }

}
