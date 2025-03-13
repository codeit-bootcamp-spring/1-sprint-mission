package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class ChannelFindDTO
{

    private UUID id;
    private ChannelType type;
    private String name;
    private Instant lastMessageAt; // 가장 최근 메시지 시간
    private List<UUID> privateUserIdList ;// PRIVATE 채널 User ID 리스트

    public ChannelFindDTO(Channel channel, Instant lastMessageAt, List<UUID> privateUserIdList) {
           this.id=channel.getId();
           this.type=channel.getType();
           this.name=channel.getChannelName();
           this.lastMessageAt=lastMessageAt;
           this.privateUserIdList=privateUserIdList;
    }
  
    //채널에 유저가 있는지 반환
    public Boolean isUserExist(UUID userId) {
            if(privateUserIdList.contains(userId)){
                return true;
            }
            return false;
    }
}
