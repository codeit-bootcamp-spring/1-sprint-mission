package com.sprint.mission.discodeit.entity;
import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable, Entity {
    private static final long serialVersionUID = 1L;

    private final long createdAt;
    private long updatedAt;
    private UUID id;
    private String userName;

    public User(String userName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.userName = userName;
    }

    //생성시간 리턴
    public long getCreatedAt(){return this.createdAt;}
    //업데이트시간 리턴
    public long getUpdatedAt(){return this.updatedAt;}
    //업데이트시간 수정
    public void setUpdatedAt(){this.updatedAt = System.currentTimeMillis();}
    //UUID 리턴
    public UUID getId(){return this.id;}
    //유저이름 리턴
    public String getUserName(){return this.userName;}
    //유저이름 변경
    public void setUserName(String userName) {
        this.userName = userName;
        this.setUpdatedAt();
    }
}
