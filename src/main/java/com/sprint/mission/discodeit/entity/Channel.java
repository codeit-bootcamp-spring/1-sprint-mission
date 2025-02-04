package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * The type Channel.
 */
public class Channel extends BaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String channelName;
    private User channelOwnerUser;
    private Map<UUID, User> channelUsers;

    // 메세지는 채널 안에 존재함
    private Map<UUID, Message> channelMessages;


    public Channel(String channelName, User channelOwnerUser, Map<UUID, User> channelUsers) {
        super();
        setChannelName(channelName);
        setChannelOwnerUser(channelOwnerUser);

        this.channelUsers = new HashMap<>();
        this.channelMessages = new HashMap<>();

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
            throw new UserNotFoundException();
        }

        channelUsers.put(user.getUserId(), user);

        setUpdatedAt();
    }


    public Message addMessageInChannel(Message addMessage) {
        checkMessage(addMessage);

        channelMessages.put(addMessage.getMessageId(), addMessage);

        Message putMessage = channelMessages.get(addMessage.getMessageId());

        setUpdatedAt();

        return putMessage;
    }

    public void removeUser(User user) {
        channelUsers.entrySet().stream()
                .filter(entry -> entry.getKey().equals(user.getUserId()))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);

        channelUsers.remove(user.getUserId());
        setUpdatedAt();
    }


    public void removeMessageInChannel(UUID messageId) {
        if (!channelMessages.containsKey(messageId)) {
            throw new MessageNotFoundException();
        }

        channelMessages.remove(messageId);
    }


    private void checkChannelName(String channelName) {
        if (channelName == null || channelName.isEmpty()) {
            throw new IllegalArgumentException("채널 이름을 작성해주세요.");
        }
    }

    private void checkChannelOwnerUser(User channelOwnerUser) {
        if (channelOwnerUser == null) {
            throw new UserNotFoundException();
        }
    }

    private void checkMessage(Message addMessage) {
        if(addMessage == null) {
            throw new MessageNotFoundException();
        }

        UUID sender = addMessage.getMessageSendUser().getUserId();
        UUID receiver = addMessage.getMessageReceiveUser().getUserId();

        if(channelOwnerUser.getUserId().equals(sender) || channelOwnerUser.getUserId().equals(receiver)) {
            return;
        }

        if (!channelUsers.containsKey(sender) || !channelUsers.containsKey(receiver)) {
            throw new UserNotFoundException();
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


    public Map<UUID, Message> getChannelMessages() {
        return channelMessages;
    }


    public Long getCreatedAt() {
        return getCreatedAtBaseObject();
    }


    public Long getUpdatedAt() {
        return getUpdatedAtBaseObject();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelOwnerUser=" + channelOwnerUser +
                ", channelUsers=" + channelUsers +
                ", channelMessages=" + channelMessages +
                '}' + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(getChannelId(), channel.getChannelId()) &&
                Objects.equals(channelName, channel.channelName) &&
                Objects.equals(channelOwnerUser, channel.channelOwnerUser) &&
                Objects.equals(channelMessages, channel.channelMessages) &&
                Objects.equals(channelUsers, channel.channelUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChannelId(), channelName, channelOwnerUser, channelUsers, channelMessages);
    }
}
