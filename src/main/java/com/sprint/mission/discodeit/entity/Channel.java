package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.UserService;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private User owner;
    private String channelName;
    private List<User> channelUserList;
    private List<Message> channelMessageList;
    //일단, 돌아가는 코드를 위해 살려두겠습니다. 리팩토링 하며 삭제예정



    public static class Builder {

        final UUID id;
        private final Long createdAt;
        private Long updatedAt;
        private User owner;
        private String channelName;
        private List<User> channelUserList;
        private List<Message> channelMessageList;

        public Builder() {
            this.id = UUID.randomUUID();
            this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            this.channelUserList = new ArrayList<>();
            this.channelMessageList = new ArrayList<>();
        }

        public Builder buildUpdatedAt() {
            this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            return this;
        }

        public Builder buildChannelName(String channelName) {
            this.channelName = channelName;
            return this;
        }
        public Builder buildOwner(User owner) {
            this.owner = owner;
            return this;
        }
        public Builder buildChannelUserList(List<User> channelUserList) {
            this.channelUserList = channelUserList;
            return this;
        }
        public Builder buildChannelMessageList(List<Message> channelMessageList) {
            this.channelMessageList = channelMessageList;
            return this;
        }



        public Channel build() {
            if (owner == null) {
                throw new IllegalArgumentException("owner is necessary");
            }
            //this.channelUserList.add(owner);
            return new Channel(this);
        }
    }

    private Channel(Channel.Builder builder) {
        this.id = builder.id;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.channelName = builder.channelName;
        this.owner = builder.owner;
        this.channelUserList = builder.channelUserList;
        this.channelMessageList = builder.channelMessageList;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public Long getUpdatedAt() {
        return updatedAt;
    }

    public User getOwner() {
        return owner;
    }
    public String getChannelName() {
        return channelName;
    }
    public List<User> getChannelUserList() {
        return channelUserList;
    }

    public List<Message> getChannelMessageList() {
        return channelMessageList;
    }



    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public void setChannelUserList(User channelUserList) {
        this.channelUserList.add(channelUserList);
    }

    public void setChannelMessageList(Message channelMessageList) {
        this.channelMessageList.add(channelMessageList);
    }

    @Override
    public String toString() {
        return "Channel ID : " + getId().toString().substring(0, 5) + " Channel Name : " + getChannelName() + " created at : " + getCreatedAt() + " Updated at : " + getUpdatedAt() + " Attendant : " + getChannelUserList() + " Chatting message : " + getChannelMessageList();
    }





}
