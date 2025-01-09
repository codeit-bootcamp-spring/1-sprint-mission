package com.sprint.mission.discodeit.entity;
import java.util.List;
import java.util.UUID;


public class User {
    private List<Message> msgList;
    private String id ;
    private String userName;
    private long createdAt;
    private long updatedAt;

    public User(String userName){
        this.id = UUID.randomUUID().toString();
        this.userName = userName;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
    }

    //get
    public String getUserId(){
        return this.id;
    }
    public String getUserName(){
        return this.userName;
    }
    public long getCreatedAt(){
        return this.createdAt;
    }
    public long getUpdatedAt(){
        return this.updatedAt;
    }
    public List<Message> getMsgList(){
        return this.msgList;
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }


}
