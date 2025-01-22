package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
    // 직렬화하려는 클래스가 Serializable 인터페이스를 구현해야 한다.
    @Serial
    private static final long serialVersionUID = 1L;
    private String nickname;

    public User(String nickname){
        if(nickname == null || nickname.trim().isEmpty()){
            throw new IllegalArgumentException("Nickname 은 공백일 수 없습니다.");
        }
        this.nickname = nickname;
    }
    public String getNickname(){
        return nickname;
    }
    public void setNickname(String nickname){
        if(nickname == null || nickname.trim().isEmpty()){
            throw new IllegalArgumentException("Nickname 은 공백일 수 없습니다.");
        }
        this.nickname = nickname;
    }
    @Override
    public String toString(){
        return "\n"
                + "nickname : " + nickname
                + "\n"
                + "create at : " + getCreatedAt()
                + "\n"
                + "updated at : " + getUpdatedAt()
                + "\n";
    }
}
