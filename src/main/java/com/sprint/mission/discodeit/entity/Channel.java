package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;


public class Channel implements Serializable, Entity {
    private static final long serialVersionUID = 1L;
    // 일부 속성 세터 추가 가능성 있어 속성마다 @Getter 명시적으로 작성.
    @Getter private final long createdAt;
    @Getter private long updatedAt;
    @Getter private UUID id;
    @Getter private String channelName;
    @Getter private ArrayList<User> members;
    @Getter private ChannelType type;
    @Getter private String description;

    public Channel(ChannelType type, String channelName, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.channelName = channelName;
        this.members = new ArrayList<>();
        this.type = type;
        this.description = description;
    }

    //todo 엔티티 객체에서 수정을 하면 업데이트시간이 바뀌어야하는데 그걸 지정하기위해 @Getter 사용은 못하는건가? (채널, 메세지, 유저 전부 해당)

    //업데이트시간 변경
    public void setUpdatedAt(){this.updatedAt = System.currentTimeMillis();}

    //채널이름 변경
    public void setChannelName(String channelName){
        this.channelName = channelName;
        this.setUpdatedAt();
    }

    //채널에 속한 멤버 리스트 반환
    public ArrayList<User> getMembers(){
        return this.members;
    }

    //채널에 속한 멤버 리스트 교체
    public void setMembers(ArrayList<User> members){
        this.members = members;
        this.setUpdatedAt();
    }

    //채널소개 변경
    public void setDescription(String description){
        this.description = description;
    }

}
