package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

// 직렬화하려는 클래스가 Serializable 인터페이스를 구현해야 한다.
public class User extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 추가할 수 있는 것 : email 과 password
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

    // setter 의 이름 변경하기
    // BaseEntity 의 refreshUpdateAt() 를 도메인 차원에서 조절해야 하나?
    // Service 레이어에서 일일이 부르는 게 뭔가 "번거롭다", "빼먹을 수도 있겠다" 라는 생각이 들긴 하네
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
                + "User id : " + getId()
                + "\n"
                + "create at : " + getCreatedAt()
                + "\n"
                + "updated at : " + getUpdatedAt()
                + "\n";
    }
}
