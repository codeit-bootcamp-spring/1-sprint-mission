package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Channel extends BaseEntity implements  Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final User admin;
    private String channelName;
    private ChannelType type;

    private final List<Message> messagesList;
    private final List<User> memberList;

    @Builder
    public Channel(ChannelType type, String channelName, User admin) {
        super();
        messagesList = new ArrayList<>();
        memberList = new ArrayList<>();
        this.channelName = channelName;
        this.admin = admin;
        this.type = type;
        memberList.add(admin);
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        update();
    }

    public void addMember(User user) {
        memberList.add(user);
    }

    public void deleteAllMember() {
        memberList.clear();
    }

    public void deleteAllMessage() {
        messagesList.clear();
    }


}
