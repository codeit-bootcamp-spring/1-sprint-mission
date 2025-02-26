package com.spirnt.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PrivateChannelRequest {
    private List<UUID> usersId;

    public PrivateChannelRequest(List<UUID> usersId) {
        this.usersId = (usersId == null) ? new ArrayList<>() : usersId;
    }
}
