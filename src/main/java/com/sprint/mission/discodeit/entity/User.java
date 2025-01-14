package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity{
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
