package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Channel extends BaseEntity implements  Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final User admin;
    private final List<User> memberList;
    private String channelName;
    private ChannelType type;


    public Channel(ChannelType type, String channelName, User admin) {
        super();
        memberList = new ArrayList<>();
        this.channelName = channelName;
        this.admin = admin;
        this.type = type;
        memberList.add(admin);
    }

    public void update(String newName) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.channelName)) {
            this.channelName = newName;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            updated();
        }
    }
    public void addMember(User user) {
        memberList.add(user);
    }

    public void deleteMember(User user) {
        memberList.remove(user);
    }

}
