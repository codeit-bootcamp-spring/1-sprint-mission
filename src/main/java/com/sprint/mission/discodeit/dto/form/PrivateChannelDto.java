package com.sprint.mission.discodeit.dto.form;

import com.sprint.mission.discodeit.dto.entity.BaseEntity;
import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.entity.ChannelGroup;
import com.sprint.mission.discodeit.dto.entity.ReadStatus;
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
        this.channelGroup = channel.getChannelGroup();
        userList.add(readStatus.getUserId());
    }
}
