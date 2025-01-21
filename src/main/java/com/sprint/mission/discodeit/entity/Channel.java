package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Channel extends BaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String channelName;
    private User channelOwnerUser;
    //private List<User> channelUsers;
    private Map<UUID, User> channelUsers;

    public Channel(String channelName, User channelOwnerUser, Map<UUID, User> channelUsers) {
        super();
        setChannelName(channelName);
        setChannelOwnerUser(channelOwnerUser);

        this.channelUsers = new HashMap<>();
        if (channelUsers != null) {
            this.channelUsers.putAll(channelUsers);
        }
    }

    public Channel(String channelName, User channelOwnerUser) {
        super();
        setChannelName(channelName);
        setChannelOwnerUser(channelOwnerUser);
        this.channelUsers = new HashMap<>();
    }

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("유저를 추가해주세요");
        }

        channelUsers.put(user.getUserId(), user);

        setUpdatedAt();
    }

    public void removeUser(User user) {
        channelUsers.entrySet().stream()
                .filter(entry -> entry.getKey().equals(user.getUserId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다."));

        channelUsers.remove(user.getUserId());
        setUpdatedAt();
    }

    private void checkChannelName(String channelName) {
        if (channelName == null || channelName.isEmpty()) {
            throw new IllegalArgumentException("채널 이름을 작성해주세요.");
        }
    }

    private void checkChannelOwnerUser(User channelOwnerUser) {
        if (channelOwnerUser == null) {
            throw new IllegalAccessError("방장을 지정해주세요.");
        }
    }

    public String updateChannelName(String updateChannelName) {
        setChannelName(updateChannelName);
        return this.channelName;
    }

    public User updateOwnerUser(User updateOwnerUser) {
        setChannelOwnerUser(updateOwnerUser);
        return this.channelOwnerUser;
    }

    public Map<UUID, User> updateChannelUsers(Map<UUID, User> updateChannelUsers) {
        if (updateChannelUsers == null) {
            this.channelUsers = new HashMap<>();
        } else {
            this.channelUsers = new HashMap<>(updateChannelUsers);
        }
        setUpdatedAt();
        return this.channelUsers;
    }

    public UUID getChannelId() {
        return getId();
    }

    public String getChannelName() {
        return channelName;
    }

    private void setChannelName(String channelName) {
        checkChannelName(channelName);
        this.channelName = channelName;
        setUpdatedAt();
    }

    public User getChannelOwnerUser() {
        return channelOwnerUser;
    }

    private void setChannelOwnerUser(User channelOwnerUser) {
        checkChannelOwnerUser(channelOwnerUser);
        this.channelOwnerUser = channelOwnerUser;
        setUpdatedAt();
    }

    public Map<UUID, User> getChannelUsers() {
        return channelUsers;
    }

    public Long getCreatedAt() {
        return getCreatedAtBaseObject();
    }

    public Long getUpdatedAt() {
        return getUpdatedAtBaseObject();
    }

    @Override
    public String toString() {
        return "Channel{" + "channelName='" + channelName + '\'' + ", channelOwnerUser=" + channelOwnerUser + ", channelUsers=" + channelUsers + ", createdAt=" + getCreatedAt() + ", updatedAt=" + getUpdatedAt() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(getChannelId(), channel.getChannelId()) &&
                Objects.equals(channelName, channel.channelName) &&
                Objects.equals(channelOwnerUser, channel.channelOwnerUser) &&
                Objects.equals(channelUsers, channel.channelUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChannelId(), channelName, channelOwnerUser, channelUsers);
    }
}
