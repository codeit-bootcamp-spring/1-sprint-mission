package com.sprint.mission.discodeit.entity;
import java.util.ArrayList;
import java.util.UUID;

public class Channel {
    private final long createdAt;
    private long updatedAt;
    private UUID id;
    private String channelName;
    private ArrayList<User> members;

    public Channel(String channelName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.channelName = channelName;
        this.members = new ArrayList<>();
    }
    //생성시간 리턴
    public long getCreatedAt(){return this.createdAt;}
    //업데이트시간 리턴
    public long getUpdatedAt(){return this.updatedAt;}
    //업데이트시간 변경
    public void setUpdatedAt(){this.updatedAt = System.currentTimeMillis();}

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
    //채널에 속한 멤버 리스트 반환
    public ArrayList<User> getMembers(){
        return this.members;
    }

    //채널에 속한 멤버 리스트 교체
    public void setMembers(ArrayList<User> members){
        this.members = members;
        this.setUpdatedAt();
    }
    //채널에 멤버 한명 추가
    public void addMember(User user){
        this.members.add(user);
        this.setUpdatedAt();
    }


}
