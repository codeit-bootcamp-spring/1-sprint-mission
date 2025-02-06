package com.sprint.mission.discodeit.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;



public class Channel implements Serializable, Entity {
    private static final long serialVersionUID = 1L;

    private final long createdAt;
    private long updatedAt;
    private UUID id;
    private String channelName;
    private ArrayList<User> members;
    private ChannelType type;

    public Channel(ChannelType type, String channelName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.channelName = channelName;
        this.members = new ArrayList<>();
        this.type = type;
    }
    //생성시간 리턴
    public long getCreatedAt(){return this.createdAt;}
    //업데이트시간 리턴
    public long getUpdatedAt(){return this.updatedAt;}
    //업데이트시간 변경
    public void setUpdatedAt(){this.updatedAt = System.currentTimeMillis();}
    //채널 uuid 리턴
    public UUID getId(){return this.id;}
    // 채널이름 리턴
    public String getChannelName(){
        return this.channelName;
    }
    //채널이름 변경
    public void setChannelName(String channelName){
        this.channelName = channelName;
        this.setUpdatedAt();
    }
    //타입 리턴
    public ChannelType getType(){
        return this.type;
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


}
