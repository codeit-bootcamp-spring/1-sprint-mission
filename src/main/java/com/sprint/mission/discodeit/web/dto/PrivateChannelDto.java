package com.sprint.mission.discodeit.web.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelGroup;
import lombok.Getter;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Getter
public class PrivateChannelDto{
    private UUID id;
    private ChannelGroup group;
    private List<UUID> userIds;
    private Instant lastMessageTime;


    public PrivateChannelDto(Channel channel) {
        this.id = channel.getId();
        this.group = channel.getGroup();
        this.userIds=channel.getParticipants().stream()
                .map(participant -> participant.getUser().getId()) // User의 ID 추출
                .collect(Collectors.toList());
    }
    public PrivateChannelDto(Channel channel, Instant lastMessageTime) {
        this.id = channel.getId();
        this.group = channel.getGroup();
        this.userIds=channel.getParticipants().stream()
                .map(participant -> participant.getUser().getId()) // User의 ID 추출
                .collect(Collectors.toList());
        this.lastMessageTime = lastMessageTime;
    }

    @Override
    public String toString() {
        return "PrivateChannelDto{" +
                "id=" + id +
                ", group=" + group +
                ", userIds=" + userIds +
                ", lastMessageTime=" + lastMessageTime +
                '}';
    }
}
