package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class Channel extends BaseEntity implements  Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final User admin;
    private final ChannelType type;
    private String channelName;
    private List<UUID> memberList;

    public Channel(ChannelType type, String channelName, User admin) {
        super();
        this.channelName = channelName;
        this.admin = admin;
        this.memberList = new ArrayList<>();
        this.type = type;
    }

    public void update(String newName) {
        boolean isUpdated = false;
        if (!newName.equals(this.channelName)) {
            this.channelName = newName;
            isUpdated = true;
        }

        if (isUpdated) {
            updated();
        }
    }
    public void addMember(User user) {
        memberList.add(user.getId());
    }

    public void deleteMember(User user) {
        memberList.remove(user.getId());
    }

}
