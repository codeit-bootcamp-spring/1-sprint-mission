package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 추가할 수 있는 것 : 채널에 소속(참가)된 사용자
        // 채널의 속성 : private, public
        // private 의 경우 owner의 초대로만 참가할 수 있다.
        // public 의 경우 1. 1.1서버의 주소/1.2서버 서칭 탭에서 서칭을 통한 접근을 통해 스스로 참가 가능하다. 2. owner의 초대로 참가
    private User owner;
    private String channelName;

    public Channel(User owner, String channelName){
        if(owner == null){
            throw new IllegalArgumentException("User 은 null 일 수 없습니다.");
        }
        if(channelName == null || channelName.trim().isEmpty()){
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없습니다.");
        }
        this.owner = owner;
        this.channelName = channelName;
    }

    public String getChannelName(){
        return channelName;
    }

    public void setChannelName(String channelName){
        if(channelName == null || channelName.trim().isEmpty()){
            throw new IllegalArgumentException("channelName 은 null 이거나 공백일 수 없습니다.");
        }
       this.channelName = channelName;
    }

    public User getUser(){return owner;}

    @Override
    public String toString(){
        return "\n"
                + "User : "  + owner.getNickname()
                + "\n"
                + "Channel : " + getChannelName()
                + "\n"
                + "create at : " + getCreatedAt()
                + "\n"
                + "Updated at : " + getUpdatedAt()
                + "\n";
    }
}
