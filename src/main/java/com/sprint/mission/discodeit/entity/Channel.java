package com.sprint.mission.discodeit.entity;
import com.sprint.mission.discodeit.entity.Type.ChannelType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public class Channel implements Serializable, Entity {
    private static final long serialVersionUID = 1L;

    private final Instant createdAt;
    @Setter private Instant updatedAt;
    private UUID id;
    @Setter private String channelName;
    @Setter private ArrayList<UUID> members;
    @Setter private ChannelType type;
    @Setter private String description;

    public Channel(ChannelType type, String channelName, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.channelName = channelName;
        this.members = new ArrayList<UUID>();
        this.type = type;
        this.description = description;
    }

    //todo 엔티티 객체에서 수정을 하면 업데이트시간이 바뀌어야하는데 그걸 지정하기위해 클래스에 @Getter 지정은 못하는건가? (엔티티 전부 해당)

    //업데이트시간 변경
    public void setUpdatedAt(){this.updatedAt = Instant.now();}

    //채널이름 변경
    public void setChannelName(String channelName){
        this.channelName = channelName;
        this.setUpdatedAt();
    }

    //채널에 속한 멤버 리스트 교체
    public void setMembers(ArrayList<UUID> members){
        this.members = members;
        this.setUpdatedAt();
    }

    //채널소개 변경
    public void setDescription(String description){
        this.description = description;
        this.setUpdatedAt();
    }

}
