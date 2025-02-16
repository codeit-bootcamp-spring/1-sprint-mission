package com.sprint.mission.discodeit.entity.form;

import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelGroup;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PrivateChannelDto extends BaseEntity {
    private ReadStatus readStatus;

    private ChannelGroup channelGroup;
    private List<UUID> userList=new ArrayList<>();

    public PrivateChannelDto(Channel channel) {
        this.readStatus = channel.getReadStatus();
        this.channelGroup = channel.getChannelGroup();
        userList.add(readStatus.getUserId());
    }
}
