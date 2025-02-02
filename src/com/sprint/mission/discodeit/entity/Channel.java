package com.sprint.mission.discodeit.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {

    List<User> userList;
    String id ;
    String chName;
    long createdAt;
    long updatedAt;

    public Channel(String chName){
        this.id = UUID.randomUUID().toString();
        this.chName = chName;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
        this.userList= new ArrayList<>();
    }

    //get
    public String getChId(){
        return this.id;
    }
    public String getChName(){
        return this.chName;
    }
    public long getCreatedAt(){
        return this.createdAt;
    }
    public long getUpdatedAt(){
        return this.updatedAt;
    }
    public List<User> getUserList(){
        return userList;
    }

    //update
    public void updateName(String name){
        this.chName=name;
    }
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }


}
