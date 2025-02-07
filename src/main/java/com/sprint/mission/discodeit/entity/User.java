package com.sprint.mission.discodeit.entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable  {

    private static final Long serialVersionUID = 1L;
    private UUID id ;
    private String userName;
    private final Long createdAt;
    private Long updatedAt;

    public User(String userName){
        this.id = UUID.randomUUID();
        this.userName = userName;
        this.createdAt =  System.currentTimeMillis() / 1000; // 초 단위로 변환
    }

    //update
    public void updateUpdatedAt(){
        this.updatedAt=System.currentTimeMillis() /1000; //업데이트 시간
    }
    public void updateUsername(String userName){
        this.userName =userName;
    }


}
