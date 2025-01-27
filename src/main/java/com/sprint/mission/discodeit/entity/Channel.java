package com.sprint.mission.discodeit.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Channel extends BaseEntity implements  Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final User admin;
    private final List<Message> messagesList;
    private final List<User> memberList;
    private String channelName;

    public Channel(String channelName, User admin) {
        super();
        messagesList = new ArrayList<>();
        memberList = new ArrayList<>();
        this.channelName = channelName;
        this.admin = admin;
        memberList.add(admin);
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
        update();
    }

    public void addMember(User user) {
        memberList.add(user);
    }

    public void addMessage(Message message) {
        messagesList.add(message);
    }

    public void deleteMember(User user) {
        memberList.remove(user);
    }

    public void deleteAllMember() {
        memberList.clear();
    }

    public void deleteMessage(Message message) {
        messagesList.remove(message);
    }

    public void deleteAllMessage() {
        messagesList.clear();
    }


}
